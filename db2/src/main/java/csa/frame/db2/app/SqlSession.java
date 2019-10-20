package csa.frame.db2.app;

import csa.frame.db2.config.DaoConfig;
import csa.frame.db2.config.ServiceConfig;

/**
 * 当前会话
 * @author csa
 *
 */
public class SqlSession {
	
	
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
