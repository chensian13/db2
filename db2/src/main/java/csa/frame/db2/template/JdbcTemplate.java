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

import javax.swing.text.WrappedPlainView;

import csa.frame.db2.annotation.Key;
import csa.frame.db2.app.WrapResultListener;
import csa.frame.db2.exception.DBOperException;
import csa.frame.db2.utils.StringUtils;
import csa.frame.db2.utils.TypeHelper;


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
					if(TypeHelper.isPrepareStatementParam(args[i])){
						ps.setObject(i+1, args[i]);
					}else if(args[i] instanceof WrapResultListener){
						//用户自定义参数封装回调,回调函数必须放在最后
						break;
					}else{
						//其它类型抛出参数不合法异常
						throw new DBOperException("传输器参数不合法异常");
					}
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
	
	
}
