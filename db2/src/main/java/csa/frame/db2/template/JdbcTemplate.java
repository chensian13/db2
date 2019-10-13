package csa.frame.db2.template;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import csa.frame.db2.annotation.Key;
import csa.frame.db2.exception.DBOperException;
import csa.frame.db2.utils.StringUtils;


public class JdbcTemplate {
	private Connection conn;
	private PreparedStatement ps;
	private ResultSet rs;
	
	
	public void setConnection(Connection conn) {
		this.conn = conn;
	}

	/**
	 * 摸吧语法
	 * @param sql
	 * @param listener
	 * @throws SQLException
	 */
	public void template(String sql,ExecuteSQLListener listener,Object...args){
		try {
			ps=conn.prepareStatement(sql);
			//参数封装
			if(args!=null){
				for(int i=0;i<args.length;i++){
					ps.setObject(i+1, args[i]);
				}
			}// end if
			listener.execute(conn, ps, rs);
		}catch(Exception e){
			//抛出查询异常
			throw new DBOperException(e);
		}finally{
			//释放资源
			if(ps!=null){
				try {
					ps.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}finally{
					ps=null;
				}
			}
		}
	}
	
	/**
	 * 回调执行接口
	 * @author csa
	 *
	 */
	public interface ExecuteSQLListener{
		void execute(Connection conn,PreparedStatement ps,ResultSet rs) throws Exception;
	}
	
	
	
	/**
	 * 
	 * @param beanhandler 结果集封装
	 * @param beanType 结果集内元素类型：实体类类型
	 * @param rs
	 * @return
	 * @throws Exception
	 */
	public static <T> void wrapResult(Beanhandler<T> beanhandler,Class beanType,ResultSet rs) throws Exception{
		//结果封装
		if(beanhandler.getType()==Beanhandler.TYPE_ARRAYLIST){
			//集合
			beanhandler.setList(new ArrayList<T>());
			while(rs.next()){
				beanhandler.getList().add((T) wrapOne(beanType,rs));
			}
		}else if(beanhandler.getType()==Beanhandler.TYPE_BEAN){
			//单个结果集
			if(rs.next()){
				beanhandler.setBean((T) wrapOne(beanType,rs));
			}
		}else if(beanhandler.getType()==Beanhandler.TYPE_COUNT){
			//记录数
			if(rs.next()){
				beanhandler.setCount(rs.getLong(1));
			}	
		}
	}
	
	//***********************************私有 工具way************************************
	/**
	 * 封装单条记录
	 * @param type 实体类类型或者是map类型封装
	 * @param rs
	 * @return
	 * @throws Exception
	 */
	private static Object wrapOne(Class type,ResultSet rs) throws Exception{
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
			return obj;
		}else{
			map=new HashMap<String,Object>();
			ResultSetMetaData data=rs.getMetaData();
			for(int i=1;i<=data.getColumnCount();i++){
				map.put(data.getColumnName(i), rs.getObject(i));
			}
			return map;
		} //end else
	}
	
	
	/**
	 * 从rs对象中提取值
	 * @param key
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	private static Object getColumnValue(Key key,ResultSet rs) throws SQLException{
		if(getColumnName(key)!=null){
			return rs.getObject(getColumnName(key));
		}
		return null;
	}
	
	/**
	 * 从rs对象中提取值
	 * @param key
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	private static Object getColumnValue(csa.frame.db2.annotation.Field field,ResultSet rs) throws SQLException{
		if(getColumnName(field)!=null){
			return rs.getObject(getColumnName(field));
		}
		return null;
	}
	
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
