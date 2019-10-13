package csa.frame.db2.utils;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 日志打印
 * @author csa
 *
 */
public class LoggerUtil {
	private static Logger logger=Logger.getGlobal();
	
	public static void info(String msg){
		logger.setLevel(Level.INFO);
		logger.info(msg);
	}
	
	
	public static void warn(String msg){
		logger.setLevel(Level.WARNING);
		logger.warning(msg);
	}

}
