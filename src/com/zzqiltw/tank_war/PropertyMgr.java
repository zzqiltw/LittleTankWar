package com.zzqiltw.tank_war;

import java.io.IOException;
import java.util.Properties;

public class PropertyMgr {
	
	static Properties prop = new Properties();
	
	static {
		try {
			prop.load(PropertyMgr.class.getClassLoader().getResourceAsStream("config/tank.properties"));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	/*
	 * 阻止其他对象new PropertyMgr
	 */
	private PropertyMgr() {};

	public static String getProperty(String key) {
		
		return prop.getProperty(key);
	}
}
