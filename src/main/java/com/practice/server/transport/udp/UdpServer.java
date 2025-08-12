package com.practice.server.transport.udp;

import com.practice.server.core.AbstractNetworkServer;

import java.io.IOException;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;

public class UdpServer extends AbstractNetworkServer {
    private DatagramSocket socket;

    public UdpServer(int port, ExecutorService workers) {
        super(port, workers);
    }

    @Override
    protected void doStartResources() throws SocketException {
        socket = new DatagramSocket(port());
        socket.setReuseAddress(true);
        socket.setSoTimeout(500); // stop のために軽くポーリング
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
}
