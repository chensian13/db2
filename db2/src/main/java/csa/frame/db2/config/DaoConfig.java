package csa.frame.db2.config;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import csa.frame.db2.annotation.SQL;
import csa.frame.db2.app.ConnectionStore;
import csa.frame.db2.app.WrapResultListener;
import csa.frame.db2.constant.ExecuteType;
import csa.frame.db2.exception.DBSqlSessionException;
import csa.frame.db2.template.Beanhandler;
import csa.frame.db2.template.JdbcTemplate;
import csa.frame.db2.template.JdbcTemplate.ExecuteSQLListener;
import csa.frame.db2.utils.LoggerUtil;
import csa.frame.db2.utils.TypeHelper;

/**
 * 解析Dao层接口
 * @author csa
 *
 */
public class DaoConfig implements InvocationHandler{
	private JdbcTemplate jdbcTemplate;
	private Connection conn;
	
	private DaoConfig() {
		jdbcTemplate=new JdbcTemplate();
	}

	/**
	 * 
	 * @param daoInterface
	 * @return
	 */
	public static <T> T newInstance(Class daoInterface){
		return (T) Proxy.newProxyInstance(DaoConfig.class.getClassLoader(), 
				new Class[]{daoInterface}, new DaoConfig());
	}

	/**
	 * 拦截接口方法进行实现：
	 * 解析方法上的注解SQl，获取sql语句和执行类型：查询或者写操作
	 * 参数封装
	 * 执行SQL
	 * 结果集封装-重点
	 */
	public Object invoke(Object proxy,  final Method method,  final Object[] args) throws Throwable {
		final SQL sql=method.getAnnotation(SQL.class);
		if(sql==null){
			return null;
		}
		//获取该线程的连接
		conn=ConnectionStore.getConnection();
		//数据库连接没有获取
		if(conn==null){
			throw new DBSqlSessionException("请先注册数据库连接：ConnectionStore.registerConnection");
		}
		LoggerUtil.info("执行sql："+sql.value());
		try{
			//初始化结果集封装对象
			final Beanhandler beanhandler=new Beanhandler();
			//注册返回类型
			beanhandler.setType(method.getReturnType());
			//注册连接,如果当前线程连接资源为空，则重新获取
			jdbcTemplate.setConnection(conn);
			jdbcTemplate.template(sql.value(), new ExecuteSQLListener(){
				public void execute(Connection conn, PreparedStatement ps, ResultSet rs) throws Exception{
					if(ExecuteType.SELECT.equals(sql.type())){
						//查询
						rs=ps.executeQuery();
						//封装查询结果
						if(args==null || args.length==0 || 
								!(args[args.length-1] instanceof WrapResultListener)){
							//框架自动封装参数
							beanhandler.wrapResult(sql.resultType(), rs);
						}else{
							//用户自定义封装参数
							WrapResultListener listener=(WrapResultListener)args[args.length-1];
							listener.wrapResult(beanhandler, rs);
						}
					}else{
						//更新
						beanhandler.setCount(ps.executeUpdate());
					} //end else
				} //end
			},args);
			//返回执行结果
			if(beanhandler.getType()==Beanhandler.TYPE_ARRAYLIST){
				return beanhandler.getList();
			}else if(beanhandler.getType()==Beanhandler.TYPE_BEAN){
				return beanhandler.getBean();
			}else if(beanhandler.getType()==Beanhandler.TYPE_COUNT){
				return TypeHelper.castNumber(beanhandler.getCount(),method.getReturnType());
			}
		}catch(Exception e){
			//打印异常
			e.printStackTrace();
		}
		return null;
	}
	

}
