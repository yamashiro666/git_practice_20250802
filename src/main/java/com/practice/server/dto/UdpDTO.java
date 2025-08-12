package com.practice.server.dto;

import com.fasterxml.jackson.annotation.JsonAlias;

public class UdpDTO {
	@JsonAlias({"name", "srvName"})
    public String srvName;
	public String ip;
    public Integer port;
}
