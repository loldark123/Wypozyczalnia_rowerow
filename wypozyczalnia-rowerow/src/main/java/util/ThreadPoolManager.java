package util;

import konfiguracja.ThreadPoolConfig;
import java.util.concurrent.*;

public class ThreadPoolManager {
    private static final ExecutorService executor = Executors.newFixedThreadPool(ThreadPoolConfig.LICZBA_WATKOW);

    public static ExecutorService getExecutor() {
        return executor;
    }

    public static void shutdown() {
        executor.shutdown();
    }

    public static void shutdownNow() {
        executor.shutdownNow();
    }
}
