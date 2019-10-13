package csa.frame.db2.template;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.sql.DataSource;

import csa.frame.db2.config.ConfigInfo;
import csa.frame.db2.exception.DBConfigException;

/**
 * 获取数据库连接对象
 * @author csa
 *
 */
public class Connect {
	private static ConfigInfo configInfo=ConfigInfo.getConfigInfo();
	static{
		try {
			//注册驱动器
			Class.forName(configInfo.getDriver());
		} catch (Exception e) {
			throw new DBConfigException("资源加载异常",e);
		}
	}
	private static DataSource dataSource;
	/**
	 * 获取数据库连接
	 * @return
	 */
	public static Connection getConnection(){
		Connection connection=null;
		try {
			if(configInfo.isPool()){
				//池连接
				return dataSource.getConnection();
			}else{
				//传统连接
				connection = DriverManager.getConnection(configInfo.getUrl(),
						configInfo.getUsername(),
						configInfo.getPassword());
			}
		} catch (SQLException e) {
			throw new DBConfigException("获取数据库连接异常",e);
		}
		return connection;
	}
	
	public static DataSource getDataSource() throws Exception{
		return dataSource;
	}

	public static void setDataSource(DataSource dataSource) {
		Connect.dataSource = dataSource;
	}
	
	
	//*****************************私有工具way******************************
	
}
