package csa.frame.db2.config;

import java.util.Properties;

import csa.frame.db2.exception.DBConfigException;

/**
 * 读取配置文件，将配置封装在对象中
 * @author csa
 *
 */
public class ConfigInfo {
	private static Properties configProperties=null;
	private static ConfigInfo configInfo;
	static{
		Config config=new Config();
		try {
			//读取数据配置文件
			configProperties=config.getConfigProperties();
		} catch (Exception e) {
			throw new DBConfigException("资源加载异常",e);
		}
	}
	
	private String url;
	private String username;
	private String password;
	private String driver;
	//是否连接池：默认否
	private boolean pool;

	
	private ConfigInfo(){
		//封装配置信息到对象
		url=configProperties.getProperty("url");
		username=configProperties.getProperty("username");
		password=configProperties.getProperty("password");
		driver=configProperties.getProperty("driver");
		
		pool=getBoolean(configProperties.getProperty("pool"));

	};
	/**
	 * 单例模式
	 * @return
	 */
	public static ConfigInfo getConfigInfo(){
		synchronized (ConfigInfo.class) {
			if(configInfo==null){
				configInfo=new ConfigInfo();
			}
			return configInfo;
		}
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getDriver() {
		return driver;
	}

	public void setDriver(String driver) {
		this.driver = driver;
	}

	public boolean isPool() {
		return pool;
	}
	public void setPool(boolean pool) {
		this.pool = pool;
	}
	
	//*****************************私有工具way*******************************
	/**
	 * 获取卑职文件中的boolean值
	 * @param string
	 * @return
	 */
	private boolean getBoolean(String string){
		if(string==null){
			return false;
		}
		if("true".equals(string)){
			return true;
		}else{
			return false;
		}
	}

}
