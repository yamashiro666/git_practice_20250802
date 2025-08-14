package com.practice.client.dto;

public class TcpDTO extends AbstractDTO<String>{
	
	String data;
	
	public void setDTO(String data) {
		this.data = data;
	}
	
	public String getDTO() {
		return this.data;
	}
}
