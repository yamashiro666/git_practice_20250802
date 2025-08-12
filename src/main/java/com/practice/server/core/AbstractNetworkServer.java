package com.practice.server.core;

import java.io.IOException;
import java.util.Objects;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import com.practice.server.api.NetworkServer;
import com.practice.server.util.ConfigLoader;

public abstract class AbstractNetworkServer implements NetworkServer {
	private final String ipAddr;
    private final int port;
    private final AtomicBoolean running = new AtomicBoolean(false);
    private final ExecutorService workers;
    private Thread acceptorThread;

    protected AbstractNetworkServer(ExecutorService workers) {
        this.workers = workers;

        Properties p = ConfigLoader.load();
        String prefix = getConfigPrefix(); // 子クラスが "tcp" / "udp" 等を返す

        this.ipAddr = ConfigLoader.get(p, prefix + ".ip", "0.0.0.0"); // 未設定は全NIC
        this.port   = ConfigLoader.getInt(p, prefix + ".port", defaultPort()); // 既定ポートを子クラスが決める
    }

    /** 子クラス固有の設定プレフィックスを返す（例: "tcp", "udp"） */
    protected abstract String getConfigPrefix();

    /** 子クラス固有のデフォルトポート（設定未指定時に使う） */
    protected abstract int defaultPort();

    @Override
    public final void start() throws IOException {
    	// サーバ起動判定
    	// サーバが未起動(false)の場合 → falseからtrueにして以降の処理実行。
    	// サーバが起動済(true)の場合 → tureのまま変更せずに、以降の処理を実行。
        if (!running.compareAndSet(false, true)) return;

        // サーバのソケット作成、ipとポートの紐づけ
        doStartResources();

        // サーバの待受〜リクエスト処理〜サーバ終了までの処理スレッドを作成
        acceptorThread = new Thread(() -> {
            try {
            	// サーバの待受〜リクエスト処理
                runLoop();
            } catch (Throwable ignored) {
            } finally {
            	// サーバ終了。起動状態にfalseを設定
                try { doStopResources(); } catch (IOException ignored) {}
                running.set(false);
            }
        }, getClass().getSimpleName() + "-Acceptor");
        // デーモンスレッドで起動するように設定。メインスレッドが終了した場合、
        // 自動でこのスレッドっも終了するようにする。
        acceptorThread.setDaemon(true);
        // スレッド起動
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

    // IPとポート番号の取得
    public final String getIpAddr() { return ipAddr; }
    public final int getPort() { return port; }

    protected ExecutorService workers() { return workers; }

    protected abstract void runLoop();
    protected abstract void doStartResources() throws IOException;
    protected abstract void doStopResources() throws IOException;
}
