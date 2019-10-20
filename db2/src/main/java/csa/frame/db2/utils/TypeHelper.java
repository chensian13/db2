package csa.frame.db2.utils;

import java.sql.Timestamp;

/**
 * 类型判断
 * @author csa
 *
 */
public class TypeHelper {
	
	/**
	 * 是否为数子（包含对应的包装类）
	 * @return
	 */
	public static boolean isNumber(Class type){
		String name=type.getName();
		if(name.equals("int") || name.equals("short") || name.equals("long")
				 || name.equals("byte") || name.equals("java.lang.Integer") 
				 || name.equals("java.lang.Short") || name.equals("java.lang.Long") 
				 || name.equals("java.lang.Byte")){
			return true;
		}
		return false;
	}
	
	/**
	 * 判断是否为java基本数据类型以及对于的包装类
	 * @param type
	 * @return
	 */
	public static boolean isBasic(Class type){
		if(isNumber(type)){
			return true;
		}
		String name=type.getName();
		if(name.equals("char") || name.equals("java.lang.Character") ||
				name.equals("boolean") || name.equals("java.lang.Boolean")){
			return true;
		}
		return false;
	}
	
	/**
	 * 将long型数字转换为对应的数字类型
	 * @param type
	 * @return
	 */
	public static Object castNumber(Long n,Class castClass){
		String name=castClass.getName();
		if(name.equals("int") || name.equals("java.lang.Integer")){
			return n.intValue();
		}
		if(name.equals("long") || name.equals("java.lang.Long")){
			return n.longValue();
		}
		if(name.equals("short") || name.equals("java.lang.Short")){
			return n.shortValue();
		}
		if(name.equals("byte") || name.equals("java.lang.Byte")){
			return n.byteValue();
		}
		return n;
	}
	
	
	/**
	 * 判断是否为传输器的参数类型,支持null类型
	 * @param c
	 * @return
	 */
	public static boolean isPrepareStatementParam(Object arg){
		if(arg==null){
			return true;
		}
		if(isBasic(arg.getClass())){
			return true;
		}
		String name=arg.getClass().getName();
		if(name.equals("java.lang.String") || name.equals("java.util.Date") ||
				name.equals("java.sql.Timestamp") || name.equals("java.sql.Date")){
			return true;
		}
		return false;
	}
	

}
