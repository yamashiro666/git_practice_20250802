package com.practice.server.transport.udp;

import com.practice.server.core.AbstractNetworkServer;

import java.io.IOException;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;

public class UdpServer extends AbstractNetworkServer {
    private DatagramSocket socket;

    public UdpServer(ExecutorService workers) {
        super(workers);
    }

    @Override
    protected void doStartResources() throws SocketException {
        InetAddress bindAddr = null;
		try {
			bindAddr = InetAddress.getByName(getIpAddr());
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		// ソケットを作成
        socket = new DatagramSocket(new InetSocketAddress(bindAddr, getPort()));
        socket.setReuseAddress(true);
        socket.setSoTimeout(500);
        System.out.println("UDP listening on " + getIpAddr() + ":" + getPort());
    }

    @Override
    protected void runLoop() {
        byte[] buf = new byte[2048];
        while (isRunning()) {
            try {
                DatagramPacket packet = new DatagramPacket(buf, buf.length);
                socket.receive(packet); // timeout で抜けて isRunning を確認
                byte[] payload = new byte[packet.getLength()];
                System.arraycopy(packet.getData(), packet.getOffset(), payload, 0, packet.getLength());
                InetAddress addr = packet.getAddress();
                int port = packet.getPort();

                workers().submit(() -> handlePacket(payload, addr, port));
            } catch (SocketTimeoutException timeout) {
                // 継続
            } catch (IOException e) {
                if (isRunning()) {
                    // 必要ならログ
                }
            }
        }
    }

    private void handlePacket(byte[] data, InetAddress addr, int port) {
        try {
            String msg = new String(data, StandardCharsets.UTF_8);
            String echo = "UDP ECHO: " + msg;
            byte[] out = echo.getBytes(StandardCharsets.UTF_8);
            DatagramPacket resp = new DatagramPacket(out, out.length, addr, port);
            socket.send(resp);
        } catch (IOException ignored) {
        }
    }

    @Override
    protected void doStopResources() {
        if (socket != null && !socket.isClosed()) socket.close();
    }

	@Override
	protected String getConfigPrefix() {
		return "udp";
	}

	@Override
	protected int defaultPort() {
		return 5001;
	}


}
