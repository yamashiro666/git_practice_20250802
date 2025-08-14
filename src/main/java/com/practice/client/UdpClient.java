package com.practice.client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import com.practice.client.dto.UdpDTO;

class UdpClient implements Client<UdpDTO> {
    private DatagramSocket socket;
    private InetAddress address;
    private int port;
    private UdpDTO dto;

    UdpClient(String host, int port, UdpDTO dto) {
        try {
            this.address = InetAddress.getByName(host);
            this.port = port;
            this.dto = dto;
        } catch (UnknownHostException e) {
            System.err.println("Invalid host: " + e.getMessage());
        }
    }

    @Override
    public void connect() {
        try {
            socket = new DatagramSocket();
            socket.connect(address, port); // UDPでもconnectは可能（送受信先を固定）
            System.out.println("UDP: Connected to " + address.getHostAddress() + ":" + port);
        } catch (SocketException e) {
            System.err.println("UDP connection failed: " + e.getMessage());
        }
    }

    @Override
    public void send() {
        try {
        	
        	// DTOをメッセージに変換
        	String message = dto.getDTO();
        	
        	// パケットを作成
            byte[] buf = message.getBytes();
            DatagramPacket packet = new DatagramPacket(buf, buf.length, address, port);
            
            // ソケットにパケット送信
            socket.send(packet);
            System.out.println("UDP: Sent -> " + message);
            
        } catch (IOException e) {
            System.err.println("UDP send error: " + e.getMessage());
        }
    }

    @Override
    public void receive() {
        try {
        	
        	// Datagramを定義
            byte[] buf = new byte[1024];
            DatagramPacket packet = new DatagramPacket(buf, buf.length);
            
            // 待受
            socket.receive(packet);
            
            // サーバからデータ受信
            String received = new String(packet.getData(), 0, packet.getLength());
            System.out.println("UDP: Received -> " + received);
            
            // 受信した情報をDTOへセット
            dto.setDTO(received);
            
        } catch (IOException e) {
            System.err.println("UDP receive error: " + e.getMessage());
        }
    }

    @Override
    public void close() {
        if (socket != null && !socket.isClosed()) {
            socket.close();
            System.out.println("UDP: Socket closed");
        }
    }
    
    // DTOのセッターとゲッター
    public void setDTO(UdpDTO dto) {
    	this.dto = dto;
    }
    
    public UdpDTO getDTO() {
    	return this.dto;
    }
}
