package csa.frame.db2.exception;

/**
 * db2资源加载异常
 * @author csa
 *
 */
public class DBConfigException extends RuntimeException{

	public DBConfigException() {
		super();
	}

	public DBConfigException(String message, Throwable cause) {
		super(message, cause);
	}

	public DBConfigException(String message) {
		super(message);
	}

	public DBConfigException(Throwable cause) {
		super(cause);
	}
	
	

}
