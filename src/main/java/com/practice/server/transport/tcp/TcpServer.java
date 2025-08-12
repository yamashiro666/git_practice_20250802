package com.practice.server.transport.tcp;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;

import com.practice.server.core.AbstractNetworkServer;

public class TcpServer extends AbstractNetworkServer {
    private ServerSocket server;

    public TcpServer(String ip, int port, ExecutorService workers) {
    	super(ip, port, workers); // 親のフィールドに保持
    }

    public TcpServer(ExecutorService workers) {
    	super(workers); // 親のフィールドに保持
    }

    @Override
    protected void doStartResources() throws IOException {
        server = new ServerSocket();
        server.setReuseAddress(true);
        InetAddress bindAddr = InetAddress.getByName(getIpAddr());
        // IPとポートをバインドする
        server.bind(new InetSocketAddress(bindAddr, getPort()));
        System.out.println("TCP listening on " + getIpAddr() + ":" + getPort());
    }

    @Override
    protected void runLoop() {
        while (isRunning()) {
            try {
                final Socket socket = server.accept(); // stop()→close()で解除
                workers().submit(() -> handleConnection(socket));
            } catch (IOException e) {
                if (isRunning()) {
                    // 必要ならログ
                }
            }
        }
    }

    private void handleConnection(Socket socket) {
        try (socket;
             var in = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
             var out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8))) {

            String line;
            while ((line = in.readLine()) != null) {
                out.write("TCP ECHO: " + line);
                out.write("\r\n");
                out.flush();
            }
        } catch (IOException ignored) {
        }
    }

    @Override
    protected void doStopResources() throws IOException {
        if (server != null && !server.isClosed()) server.close();
    }

	@Override
	protected String getConfigPrefix() {
		return "tcp";
	}

	@Override
	protected int defaultPort() {
		return 5000;
	}

}
