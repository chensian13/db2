package csa.frame.db2.template;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import csa.frame.db2.annotation.Key;
import csa.frame.db2.utils.StringUtils;
import csa.frame.db2.utils.TypeHelper;

/**
 * 结果集封装
 * @author csa
 *
 * @param <T>
 */
public class Beanhandler<T> {
	public static int TYPE_VOID=0;
	public static int TYPE_ARRAYLIST=1;
	public static int TYPE_BEAN=2;
	public static int TYPE_COUNT=3;
	private T bean;
	private List<T> list;
	//影响行数或者记录数
	private long count;
	//结果类型
	private int type;
	
	
	public int getType() {
		return type;
	}
	public long getCount() {
		return count;
	}
	public void setCount(long count) {
		this.count = count;
	}
	public T getBean() {
		return bean;
	}
	public void setBean(T bean) {
		this.bean = bean;
	}
	public List<T> getList() {
		return list;
	}
	public void setList(List<T> list) {
		this.list = list;
	}
	

	//*********************************工具way*********************************
	public void setType(Class resultType){
		if(Collection.class.isAssignableFrom(resultType)){
			//集合
			this.type=TYPE_ARRAYLIST;
		}else if(TypeHelper.isNumber(resultType)){
			//数量
			this.type=TYPE_COUNT;
		}else if(resultType==void.class){
			//void
			this.type=TYPE_VOID;
		}else{
			//实体类型
			this.type=TYPE_BEAN;
		}
	}
	
	/**
	 * 
	 * @param beanhandler 结果集封装
	 * @param beanType 结果集内元素类型：实体类类型
	 * @param rs
	 * @return
	 * @throws Exception
	 */
	public void wrapResult(Class beanType,ResultSet rs) throws Exception{
		//结果封装
		if(this.getType()==Beanhandler.TYPE_ARRAYLIST){
			//集合
			this.setList(new ArrayList<T>());
			while(rs.next()){
				this.getList().add(wrapOne(beanType,rs));
			}
		}else if(this.getType()==Beanhandler.TYPE_BEAN){
			//单个结果集
			if(rs.next()){
				this.setBean(wrapOne(beanType,rs));
			}
		}else if(this.getType()==Beanhandler.TYPE_COUNT){
			//记录数
			if(rs.next()){
				this.setCount(rs.getLong(1));
			}	
		}
	}
	
	/**
	 * 封装单条记录
	 * @param type 实体类类型或者是map类型封装
	 * @param rs
	 * @return
	 * @throws Exception
	 */
	public T wrapOne(Class type,ResultSet rs) throws Exception{
		Object obj=null;
		Map<String,Object> map=null;
		//判断实体类是否为map
		if(!Map.class.isAssignableFrom(type)){
			obj=type.newInstance();
			//获取实体类属性
			Field[] fields = type.getDeclaredFields();
			csa.frame.db2.annotation.Field fieldAn =null;
			Key keyAn=null;
			for (Field field : fields) {
				field.setAccessible(true);
				//获取注解
				fieldAn = field.getAnnotation(csa.frame.db2.annotation.Field.class);
				keyAn=field.getAnnotation(Key.class);
				//依据查询列名映射
				String col=null;
				if(fieldAn!=null){
					col=getColumnName(fieldAn);
				}else if(keyAn!=null){
					col=getColumnName(keyAn);
				}else{
					//注解不存在不考虑参数封装
					break;
				}
				
				try{
					//属性映射
					field.set(obj, rs.getObject(col));
				}catch(SQLException e){
					break;
				}
			} //end for
			return (T)obj;
		}else{
			map=new HashMap<String,Object>();
			ResultSetMetaData data=rs.getMetaData();
			for(int i=1;i<=data.getColumnCount();i++){
				map.put(data.getColumnName(i), rs.getObject(i));
			}
			return (T)map;
		} //end else
	}
	
	//******************************私有工具way***************************
	/**
	 * 获取注解中的主键列名
	 * @param field
	 * @return
	 */
	private static String getColumnName(Key key){
		if(!StringUtils.isEmpty(key.value())){
			return key.value();
		}
		if(!StringUtils.isEmpty(key.column())){
			return key.column();
		}
		return null;
	}
	
	/**
	 * 获取注解中的列名
	 * @param field
	 * @return
	 */
	private static String getColumnName(csa.frame.db2.annotation.Field field){
		if(!StringUtils.isEmpty(field.value())){
			return field.value();
		}
		if(!StringUtils.isEmpty(field.column())){
			return field.column();
		}
		return null;
	}
}
