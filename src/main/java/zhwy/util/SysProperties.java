package zhwy.util;
 
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
 

/** 
 * 读取实时的配置文件信息
 * @author frank
 * @date 2015-07-02 
 */  
@SuppressWarnings("AliEqualsAvoidNull")
public class SysProperties {
 
	private static Logger log = LoggerFactory.getLogger(SysProperties.class);
 
	private static Properties SysLocalPropObject = null;
 
	//配置文件路径
	private static String defaultPropFileName = "/address.properties";
	//文件更新标识
	protected long lastModifiedData = -1;
	
	private static SysProperties instance;
	
	
//	public static void main(String[] args){
//		SysProperties properties = getInstance("config.properties");
//		String filename = properties.getProperty("filename");
//		System.out.println(filename);
//	}
	
	
	public static SysProperties getInstance(String fileName){
		 if(instance == null){
			 if(fileName.length()==0){
				 defaultPropFileName=fileName;
			 }
			 instance = new SysProperties();
		 }
		 return instance;
	}
	
 
	/**
	 * @description 私有构造器启动一个线程实时监控配置文件
	 */
	private SysProperties() {
		SysLocalPropObject = new Properties();
		String tempPath = this.getClass().getResource(defaultPropFileName).getFile();
		File tempFile = new File(tempPath);
		final String filePath;
		if(tempFile.exists()) {
			filePath = tempPath;
		} else {
			filePath = "system.properties";
		}
		
		final SysProperties self = this;
		File propertyFile = new File(filePath);
		if (propertyFile.exists()) {
            reloadFile(propertyFile);
        }
 
		//循环监控配置文件的变化，一旦发现文件发生变化了则重读配置文件
		Thread t = new Thread() {
			public void run() {
				while (true) {
					//间隔1秒
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
					}
					
					try {
						File propertyFile = new File(filePath);
						if (self.lastModifiedData != propertyFile.lastModified()) {
							self.reloadFile(propertyFile);
							self.lastModifiedData = propertyFile.lastModified();
						}
					} catch (Exception e) {
 
					}
				}
			}
		};
		t.start();
	}
 
	/** 
	 * 重新加载文件
	 * @author frank 2015-07-02
	 * @param propertyFile
	 */ 
	private void reloadFile(File propertyFile) {
		FileInputStream inputStreamLocal = null;
		try {
			inputStreamLocal = new FileInputStream(propertyFile);
			SysLocalPropObject.load(inputStreamLocal);
		} catch (Exception e) {
			if (e instanceof FileNotFoundException) {
				log.info("No Local Properties File Found");
				SysLocalPropObject = null;
			} else {
				e.printStackTrace();
			}
		} finally {
			try {
				if (inputStreamLocal != null) {
                    inputStreamLocal.close();
                }
			} catch (IOException e) {
				log.info("Exception is happened when to close file stream");
			}
		}
	}
 
 
	/** 
	 * 根据key获取value
	 * @author frank 2015-07-02
	 * @param property
	 * @return String
	 */ 
	public String getProperty(String property) {
		String val = null;
 
		if (SysLocalPropObject != null) {
            val = SysLocalPropObject.getProperty(property);
        }
 
		return (val);
 
	}
 
	/** 
	 * 根据key获取value
	 * @author frank 2015-07-02
	 * @param property
	 * @param defaultValue 指定默认值
	 * @return String
	 */ 
	public String getProperty(String property, String defaultValue) {
		String val = null;
 
		if (SysLocalPropObject != null) {
			val = SysLocalPropObject.getProperty(property, defaultValue);
		} else {
			val = defaultValue;
		}
 
		return (val);
	}
 
	/** 
	 * 根据key获取value
	 * @author frank 2015-07-02
	 * @param property
	 * @return Integer
	 */ 
	public Integer getIntProperty(String property) {
		String val = getProperty(property);
		Integer nVal = null;
		if (val != null) {
			try {
				nVal = Integer.parseInt(val);
			} catch (Exception e) {
	
			}
		}
		return nVal;
 
	}
 
	/** 
	 * 根据key获取value
	 * @author frank 2015-07-02
	 * @param property
	 * @param defaultValue 指定默认值
	 * @return Integer
	 */ 
	public Integer getIntProperty(String property, Integer defaultValue) {
		Integer val = getIntProperty(property);
 
		if (val == null) {
			val = defaultValue;
		}
 
		return (val);
	}
 
	/** 
	 * 根据key获取value
	 * @author frank 2015-07-02
	 * @param property
	 * @return
	 */ 
	public Long getLongProperty(String property) {
		String val = getProperty(property);
		Long nVal = null;
		try {
			nVal = Long.parseLong(val);
		} catch (Exception e) {
 
		}
		return nVal;
 
	}
 
	/** 
	 * 根据key获取value
	 * @author frank 2015-07-02
	 * @param property
	 * @param defaultValue
	 * @return
	 */ 
	public Long getLongProperty(String property, Long defaultValue) {
		Long val = getLongProperty(property);
 
		if (val == null) {
			val = defaultValue;
		}
 
		return (val);
	}
 
	/** 
	 * 根据key获取value
	 * @author frank 2015-07-02
	 * @param property
	 * @param defaultValue
	 * @return
	 */ 
	public boolean getBooleanProperty(String property, boolean defaultValue) {
		boolean retval = false;
		String val = getProperty(property);
 
		if (val == null || val.equals("")) {
            retval = defaultValue;
        } else if (val.trim().equalsIgnoreCase("true") || val.trim().equals("1")) {
            retval = true;
        }
 
		return (retval);
	}
	
	/** 
	 * 根据key获取value
	 * @author frank 2015-07-02
	 * @param property
	 * @return
	 */ 
	public Double getDoubleProperty(String property) {
		String val = getProperty(property);
		Double nVal = null;
		try {
			nVal = Double.parseDouble(val);
		} catch (Exception e) {
		}
		return nVal;
	}
 
 
	/** 
	 * 根据key获取value
	 * @author frank 2015-07-02
	 * @param property
	 * @param defaultValue
	 * @return
	 */ 
	public Double getDoubleProperty(String property, Double defaultValue) {
		Double val = getDoubleProperty(property);
		if (val == null) {
			val = defaultValue;
		}
		return (val);
	}
}
