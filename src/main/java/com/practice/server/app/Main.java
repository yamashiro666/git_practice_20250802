package com.practice.server.app;

import com.practice.server.transport.tcp.TcpServer;
import com.practice.server.transport.udp.UdpServer;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    public static void main(String[] args) throws Exception {
        ExecutorService tcpWorkers = Executors.newFixedThreadPool(Math.max(2, Runtime.getRuntime().availableProcessors()));
        ExecutorService udpWorkers = Executors.newFixedThreadPool(Math.max(2, Runtime.getRuntime().availableProcessors()));

        TcpServer tcp = new TcpServer(tcpWorkers);
        UdpServer udp = new UdpServer(udpWorkers);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try { tcp.stop(); } catch (Exception ignored) {}
            try { udp.stop(); } catch (Exception ignored) {}
        }));

        tcp.start();
        udp.start();

        System.out.println("Servers started. Ctrl+C to stop.");
        Thread.currentThread().join();
    }
}
