package csa.frame.db2.constant;

/**
 * 数据库隔离级别
 * @author csa
 *
 */
public class IsolateLevel {
	public static final int NO_TRANSACTION=0;
	public static final int READ_UNCOMMITTED=1;
	public static final int READ_COMMITTED=2;
	public static final int REPEATABLE=3;
	public static final int SERIALIZABLE=4;

}
