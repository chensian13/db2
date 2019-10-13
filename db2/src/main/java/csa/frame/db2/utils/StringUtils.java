package csa.frame.db2.utils;

public class StringUtils {
	
	/**
	 * 字符串第一个字母小写
	 * @param s
	 * @return
	 */
	public static String firstLower(String s){
		if(s.length()<=1){
			return s.toLowerCase();
		}
		return s.substring(0, 1).toLowerCase()+s.substring(1);
	}
	
	/**
	 * 字符串是否为空或者空字符串
	 * @param s
	 * @return
	 */
	public static boolean isEmpty(String s){
		if(s==null || "".equals(s.trim())){
			return true;
		}
		return false;
	}

}
