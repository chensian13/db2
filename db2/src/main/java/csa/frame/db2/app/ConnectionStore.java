package csa.frame.db2.app;

import java.sql.Connection;
import java.sql.SQLException;

import csa.frame.db2.exception.DBSqlSessionException;
import csa.frame.db2.template.Connect;
import csa.frame.db2.utils.LoggerUtil;

/**
 * 线程本地数据库连接
 * @author csa
 *
 */
public class ConnectionStore {
	//线程本地connection对象存储
	private static ThreadLocal<Connection> threadLocalConnection=new ThreadLocal<Connection>();
	
	/**
	 * 获取当前会话数据库连接
	 * @return
	 */
	public static Connection getConnection() {
		return threadLocalConnection.get();
	}
	/**
	 * 存储当前会话数据库连接，并返回
	 * @return
	 */
	public static Connection registerConnection() {
		if(getConnection()==null){
			//当前线程连接不存在
			threadLocalConnection.set(Connect.getConnection());
			LoggerUtil.info("线程 "+Thread.currentThread().getName()+"注册数据库连接对象成功");
		}
		return getConnection();
	}
	/**
	 * 是否当前线程数据库连接
	 * @throws SQLException
	 */
	public static void closeConnection() {
		try {
			LoggerUtil.info("线程 "+Thread.currentThread().getName()+"释放数据库连接对象");
			getConnection().close();
		} catch (SQLException e) {
			throw new DBSqlSessionException("数据库连接释放异常！",e);
		} finally{
			threadLocalConnection.remove();
		}
	}

}
