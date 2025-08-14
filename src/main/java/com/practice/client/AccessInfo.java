package com.practice.client;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class AccessInfo {

	// 有効無効フラグ
	private boolean enabled;
	// プロトコル
	private String protocol;
	// IPアドレス
	private String ip;
	// ポート番号
	private int port;
	// コネクション数
	private int connectionCount;


	// getter,setter(有効無効フラグ)
	public boolean getEnabled() {
		return enabled;
	}
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	// getter,setter(プロトコル)
	public String getProtocol() {
		return protocol;
	}
	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	// getter,setter(IPアドレス)
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}

	// getter,setter(ポート番号)
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}

	// getter,setter(コネクション数)
	public int getConnectionCount() {
		return connectionCount;
	}
	public void setConnectionCount(int connectionCount) {
		this.connectionCount = connectionCount;
	}

	public Map<String, AccessInfo> loadAccessProp(String path) throws IOException {
	    Properties props = new Properties();
	    try (FileInputStream fis = new FileInputStream(path)) {
	        props.load(fis);
	    }

	    Map<String, AccessInfo> map = new HashMap<>();
	    for (String name : props.stringPropertyNames()) {
	        String[] values = props.getProperty(name).split(",");
	        AccessInfo info = new AccessInfo();
	        info.setEnabled("1".equals(values[0]));
	        info.setProtocol(values[1]);
	        info.setIp(values[2]);
	        info.setPort(Integer.parseInt(values[3]));
	        info.setConnectionCount(Integer.parseInt(values[4]));
	        map.put(name, info);
	    }
	    return map;
	}
	
//    public static AccessInfo loadAccessProp(String filePath) {
//        try {
//            ObjectMapper mapper = new ObjectMapper();
//            return mapper.readValue(new File(filePath), AccessInfo.class);
//        } catch (Exception e) {
//            throw new RuntimeException("Failed to load access info: " + e.getMessage(), e);
//        }
//    }
}
