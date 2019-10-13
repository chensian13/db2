package csa.frame.db2.exception;

/**
 * 数据库操作异常
 * @author csa
 *
 */
public class DBOperException extends RuntimeException{

	public DBOperException() {
		super();
	}

	public DBOperException(String message, Throwable cause) {
		super(message, cause);
	}

	public DBOperException(String message) {
		super(message);
	}

	public DBOperException(Throwable cause) {
		super(cause);
	}
	

}
