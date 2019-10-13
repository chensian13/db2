package csa.frame.db2.app;

import java.sql.Connection;

import csa.frame.db2.config.DaoConfig;
import csa.frame.db2.config.ServiceConfig;
import csa.frame.db2.exception.DBSqlSessionException;

public class SqlSession {
	private Connection conn;

	public SqlSession() {
		this.conn = ConnectionStore.getConnection();
		if(this.conn==null){
			throw new DBSqlSessionException("当前会话数据库连接为空！");
		}
	}
	
	/**
	 * 获取dao操作对象，支持事务
	 * @param daoInterface
	 * @return
	 */
	public <T> T getDao(Class daoInterface){
		return DaoConfig.newInstance(daoInterface);
	}
	
	/**
	 * 获取业务对象
	 * @param service
	 * @return
	 */
	public static <T> T getService(Class service){
		return ServiceConfig.newInstance(service);
	}
	
	

}
