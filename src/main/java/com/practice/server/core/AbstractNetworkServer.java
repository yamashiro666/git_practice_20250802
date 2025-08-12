package com.practice.server.core;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import com.practice.server.api.NetworkServer;

public abstract class AbstractNetworkServer implements NetworkServer {
    private final int port;
    private final AtomicBoolean running = new AtomicBoolean(false);
    private final ExecutorService workers;
    private Thread acceptorThread;

    protected AbstractNetworkServer(int port, ExecutorService workers) {
        this.port = port;
        this.workers = Objects.requireNonNull(workers, "workers");
    }

    @Override
    public final void start() throws IOException {
        if (!running.compareAndSet(false, true)) return;
        doStartResources(); // bind/作成

        // リクエストに応じて処理を実行する
        acceptorThread = new Thread(() -> {
            try {
                runLoop();          // サブクラスの受け付けループ
            } catch (Throwable ignored) {
            } finally {
                try { doStopResources(); } catch (IOException ignored) {}
                running.set(false);
            }
        }, getClass().getSimpleName() + "-Acceptor");
        acceptorThread.setDaemon(true);
        acceptorThread.start();
    }

    @Override
    public final void stop() throws IOException {
        if (!running.compareAndSet(true, false)) return;
        doStopResources();           // accept/receive を解除

        // 他のスレッドの処理が完了するまで待つ。
        if (acceptorThread != null) {
            try { acceptorThread.join(2000); } catch (InterruptedException ignored) {}
        }

        // 停止処理実行
        workers.shutdown();
        try { workers.awaitTermination(2, TimeUnit.SECONDS); } catch (InterruptedException ignored) {}
    }

    @Override
    public final boolean isRunning() { return running.get(); }

    @Override
    public final int port() { return port; }

    protected ExecutorService workers() { return workers; }

    protected abstract void runLoop();
    protected abstract void doStartResources() throws IOException;
    protected abstract void doStopResources() throws IOException;
}
