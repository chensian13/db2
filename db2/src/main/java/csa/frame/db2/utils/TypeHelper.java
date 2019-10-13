package csa.frame.db2.utils;

/**
 * 类型判断
 * @author csa
 *
 */
public class TypeHelper {
	
	/**
	 * 是否为数子
	 * @return
	 */
	public static boolean isNumber(Class type){
		if(type.getName().equals("int") || type.getName().equals("short") || type.getName().equals("long")
				 || type.getName().equals("byte") || type.getName().equals("java.lang.Integer") 
				 || type.getName().equals("java.lang.Short") || type.getName().equals("java.lang.Long") 
				 || type.getName().equals("java.lang.Byte")){
			return true;
		}
		return false;
	}
	
	/**
	 * 转换为对应的数字类型
	 * @param type
	 * @return
	 */
	public static Object castNumber(Long n,Class castClass){
		if(castClass.getName().equals("int") || castClass.getName().equals("java.lang.Integer")){
			return n.intValue();
		}
		if(castClass.getName().equals("long") || castClass.getName().equals("java.lang.Long")){
			return n.longValue();
		}
		if(castClass.getName().equals("short") || castClass.getName().equals("java.lang.Short")){
			return n.shortValue();
		}
		if(castClass.getName().equals("byte") || castClass.getName().equals("java.lang.Byte")){
			return n.byteValue();
		}
		return n;
	}

}
