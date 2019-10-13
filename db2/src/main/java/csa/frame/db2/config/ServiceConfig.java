package csa.frame.db2.config;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.SQLException;

import csa.frame.db2.annotation.Transaction;
import csa.frame.db2.app.ConnectionStore;
import csa.frame.db2.constant.IsolateLevel;
import csa.frame.db2.exception.DBSqlSessionException;
import csa.frame.db2.utils.LoggerUtil;

/**
 * 业务配置，设置事务
 * @author csa
 *
 */
public class ServiceConfig implements InvocationHandler{
	private Object serviceObj;
	private Connection conn;
	
	public ServiceConfig(Class service) {
		try {
			this.serviceObj = service.newInstance();
		} catch (Exception e) {
			throw new DBSqlSessionException("service实例化异常",e);
		}
	}

	public static <T> T newInstance(Class service){
		return (T)Proxy.newProxyInstance(ServiceConfig.class.getClassLoader(), service.getInterfaces(), 
				new ServiceConfig(service));
	}
	
	/**
	 * 相当于一个业务，加上事务增强
	 */
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		Transaction transaction=method.getAnnotation(Transaction.class);
		Object returnObj=null;
		//注册连接
		conn=ConnectionStore.registerConnection();
		//方法设置了事务
		if(transaction!=null){
			//设置隔离级别
			setIsolate(transaction.isolate());
			try{
				returnObj=method.invoke(serviceObj, args);
				conn.commit();
			}catch(Exception e){
				e.printStackTrace();
				//失败回滚
				conn.rollback();
				LoggerUtil.warn("事务执行异常！回滚");
			}finally{
				//释放资源
				LoggerUtil.info("事务结束！释放连接资源");
				ConnectionStore.closeConnection();
			}
		}else{
			//此方法没有设置事务
			try{
				returnObj=method.invoke(serviceObj, args);
			}finally{
				//释放资源
				ConnectionStore.closeConnection();
				LoggerUtil.info("业务方法释放连接资源");
			}
		}
		return returnObj;
	}
	
	//***********************************私有工具way*********************************
	private void setIsolate(int isolateLevel) throws SQLException{
		if(IsolateLevel.NO_TRANSACTION==isolateLevel){
			conn.setTransactionIsolation(Connection.TRANSACTION_NONE);
		}else if(IsolateLevel.READ_UNCOMMITTED==isolateLevel){
			conn.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
		}else if(IsolateLevel.READ_COMMITTED==isolateLevel){
			conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
		}else if(IsolateLevel.REPEATABLE==isolateLevel){
			conn.setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);
		}else if(IsolateLevel.SERIALIZABLE==isolateLevel){
			conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
		}
		//设置手动提交
		if(IsolateLevel.NO_TRANSACTION!=isolateLevel){
			//只要设置了隔离
			conn.setAutoCommit(false);
		}
	}

}
