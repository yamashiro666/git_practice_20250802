package com.practice.server.admin;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.practice.server.api.NetworkServer;
import com.practice.server.transport.tcp.TcpServer;
import com.practice.server.transport.udp.UdpServer;

/**
 * TCP/UDP サーバのライフサイクルをまとめて管理するシングルトン。
 */
@org.springframework.stereotype.Component
public class ServerManager {
    private final Map<String, NetworkServer> servers = new ConcurrentHashMap<>(); // key: "tcp" / "udp"
    private final Map<String, ExecutorService> pools = new ConcurrentHashMap<>();

    public synchronized void startTcp(String srvName, String ip, int port) throws IOException {
        stop("tcp");
        ExecutorService pool = Executors.newFixedThreadPool(Math.max(2, Runtime.getRuntime().availableProcessors()));
        pools.put("tcp", pool);
        NetworkServer tcp = new TcpServer(srvName, ip, port, pool);
        tcp.start();
        servers.put("tcp", tcp);
    }

    public synchronized void startUdp(String srvName, String ip, int port) throws IOException {
        stop("udp");
        ExecutorService pool = Executors.newFixedThreadPool(Math.max(2, Runtime.getRuntime().availableProcessors()));
        pools.put("udp", pool);
        NetworkServer udp = new UdpServer(srvName, ip, port, pool);
        udp.start();
        servers.put("udp", udp);
    }

    public synchronized void stop(String kind) {
        NetworkServer s = servers.remove(kind);
        if (s != null) {
            try { s.stop(); } catch (IOException ignored) {}
        }
        ExecutorService p = pools.remove(kind);
        if (p != null) p.shutdownNow();
    }

    public synchronized void stopAll() {
        stop("tcp");
        stop("udp");
    }

    public boolean isRunning(String kind) {
        NetworkServer s = servers.get(kind);
        return s != null && s.isRunning();
    }

    public Integer port(String kind) {
        NetworkServer s = servers.get(kind);
        return (s == null) ? null : s.getPort();
    }
}