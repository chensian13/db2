package csa.frame.db2.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import csa.frame.db2.exception.DBConfigException;

/**
 * 读取配置文件
 * 已经不同路径类型获取：src，文件系统，网络等
 * @author csa
 *
 */
public class Config {
	//配置文件默认路径,优先级最高从下标为0开始
	public static final String[] paths={
		Config.class.getResource("/").getPath(),  //类路径
		"src"+File.separator //src
	};
	private List<File> configs=new ArrayList<File>(2);
	
	/**
	 * 获取配置信息
	 * @param pathType 路径类型
	 * @return
	 * @throws IOException 
	 */
	public Properties getConfigProperties() throws IOException{
		getConfigs();
		if(configs.size()==0){
			//抛出配置文件不存在异常
			throw new DBConfigException("配置文件不存在");
		}
		InputStream in=null;
		InputStreamReader isr=null;
		Properties pros=null;
		try{
			in=new FileInputStream(configs.get(0));
			isr=new InputStreamReader(in, "UTF-8");
			pros=new Properties();
			pros.load(isr);
		}finally{
			//资源有开合
			isr.close();
		}
		return pros;
	}
	
	
	/**
	 * 获取资源文件数组
	 * @param pathType
	 * @return
	 */
	public List<File> getConfigs() {
		for (String path : paths) {
			//读取配置文件
			readSrc(new File(path));
			if(configs.size()!=0){
				return configs;
			}
		}
		throw new DBConfigException("配置文件不存在！");
	}
	
	
	//***********************************private 内部方法*****************************************
	/**
	 * 读曲src路径下的配置问件
	 */
	private void readSrc(File path){
		File[] files=path.listFiles();
		for (File file : files) {
			if(file.isFile()){
				//推荐配置文件命名方式：包含db2
				if(file.toString().endsWith(".properties") && file.toString().contains("db2")){
					configs.add(file);
				}
			}else{
				readSrc(file);
			}
		} //end for
	}
	
	

}
