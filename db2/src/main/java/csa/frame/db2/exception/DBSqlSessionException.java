package csa.frame.db2.exception;

/**
 * 会话异常
 * @author csa
 *
 */
public class DBSqlSessionException extends RuntimeException{

	public DBSqlSessionException() {
		super();
	}

	public DBSqlSessionException(String message, Throwable cause) {
		super(message, cause);
	}

	public DBSqlSessionException(String message) {
		super(message);
	}

	public DBSqlSessionException(Throwable cause) {
		super(cause);
	}
	
	

}
