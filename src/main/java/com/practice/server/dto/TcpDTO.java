package com.practice.server.dto;

import com.fasterxml.jackson.annotation.JsonAlias;

public class TcpDTO {
	@JsonAlias({"name", "tcpName", "srvName"})
    public String srvName;
    public String ip;
    public Integer port;
}
