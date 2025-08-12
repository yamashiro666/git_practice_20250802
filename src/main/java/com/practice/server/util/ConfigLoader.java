package com.practice.server.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.Properties;

public final class ConfigLoader {
    private ConfigLoader() {}

    /** クラスパス上の server.properties を読む。見つからなければ空の Properties を返す。 */
    public static Properties load() {
        Properties p = new Properties();
        try (InputStream in = Thread.currentThread()
                .getContextClassLoader()
                .getResourceAsStream("server.properties")) {
            if (in != null) {
                p.load(in);
            }
        } catch (IOException ignored) {
        }
        return p;
    }

    public static String get(Properties p, String key, String defaultValue) {
        return Objects.requireNonNullElse(p.getProperty(key), defaultValue);
    }

    public static int getInt(Properties p, String key, int defaultValue) {
        String v = p.getProperty(key);
        if (v == null) return defaultValue;
        try {
            return Integer.parseInt(v.trim());
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
}
