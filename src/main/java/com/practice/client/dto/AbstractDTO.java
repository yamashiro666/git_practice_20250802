package com.practice.client.dto;

public abstract class AbstractDTO<T> {
	
	T dto;
	
	void setDTO(T dto){
		this.dto = dto;
	}
	T getDTO(){
		return this.dto;
	}

}
