package csa.frame.db2.app;

/**
 * 会话工厂
 * @author csa
 *
 */
public class SqlSessionFactory{


	/**
	 * 打开一个会话
	 * @return
	 */
	public static SqlSession openSqlSession(){
		ConnectionStore.registerConnection();
		return new SqlSession();
	}
	
	
	

	
	
	
}
