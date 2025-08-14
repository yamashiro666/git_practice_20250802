package com.practice.client;

import com.practice.client.dto.TcpDTO;
import com.practice.client.dto.UdpDTO;

class ClientFactory {
	static Client create(AccessInfo info) {
        return switch (info.getProtocol().toUpperCase()) {
            case "TCP" -> new TcpClient(info.getIp(), info.getPort(), new TcpDTO());
            case "UDP" -> new UdpClient(info.getIp(), info.getPort(), new UdpDTO());
            default -> throw new IllegalArgumentException("Unsupported protocol");
        };
    }
}
