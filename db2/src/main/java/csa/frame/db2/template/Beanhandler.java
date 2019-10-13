package csa.frame.db2.template;

import java.util.Collection;
import java.util.List;

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
}
