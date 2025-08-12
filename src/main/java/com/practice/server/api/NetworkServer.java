package com.practice.server.api;

import java.io.IOException;

public interface NetworkServer extends AutoCloseable {
    void start() throws IOException;
    void stop() throws IOException;
    boolean isRunning();
//    int getPort();
    @Override default void close() throws IOException { stop(); }
}
