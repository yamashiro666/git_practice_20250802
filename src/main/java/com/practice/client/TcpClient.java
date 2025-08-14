package com.practice.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import com.practice.client.dto.TcpDTO;

class TcpClient implements Client<TcpDTO> {
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private String host;
    private int port;
    private TcpDTO dto;

    TcpClient(String host, int port, TcpDTO dto) {
        this.host = host;
        this.port = port;
        this.dto = dto;
    }

    @Override
    public void connect() {
        try {
            System.out.println("TCP: Connecting to " + host + ":" + port);
            socket = new Socket(host, port);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            System.out.println("TCP: Connected");
        } catch (IOException e) {
            System.err.println("TCP connection failed: " + e.getMessage());
        }
    }

    @Override
    public void send() {
    	
    	// DTOからメッセージを取得
    	String message = dto.getDTO();
    	
        if (out != null) {
            out.println(message);
            System.out.println("TCP: Sent -> " + message);
        }
        
    }

    @Override
    public void receive() {
        try {
        	
        	StringBuilder sb = new StringBuilder();
        	
            while (in != null) {
            	
            	// ソケットからデータを読み込み
            	sb.append(in.readLine());
            	
            }
            
            // DTOにメッセージを設定
            System.out.println("TCP: Received -> " + sb.toString());
            dto.setDTO(sb.toString());
            
        } catch (IOException e) {
            System.err.println("TCP receive error: " + e.getMessage());
        }
    }

    @Override
    public void close() {
        try {
            if (socket != null) socket.close();
            System.out.println("TCP: Connection closed");
        } catch (IOException e) {
            System.err.println("TCP close error: " + e.getMessage());
        }
    }
    
    
    // DTOのセッターとゲッター
    public void setDTO(TcpDTO dto) {
    	this.dto = dto;
    }
    
    public TcpDTO getDTO() {
    	return this.dto;
    }
}
