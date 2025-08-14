package com.practice.client.dto;

public class UdpDTO extends AbstractDTO<String>{
	
	String data;
	
	public void setDTO(String data) {
		this.data = data;
	}
	
	public String getDTO() {
		return this.data;
	}
	
}
