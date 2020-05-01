package com.poker;

import java.util.concurrent.*;

public class ConsoleInput {
    private final int timeout;
    private final int period;

    public ConsoleInput(int timeout, int period) {
        this.timeout = timeout;
        this.period = period;
    }

    public String readLine() throws InterruptedException {
        ExecutorService ex = Executors.newSingleThreadExecutor();
        String input = null;
        try {
            Future<String> result = ex.submit(new ConsoleInputReadTask(timeout, period));
            try {
                input = result.get(timeout, TimeUnit.SECONDS);
            } catch (ExecutionException e) {
                e.getCause().printStackTrace();
            } catch (TimeoutException e) {
                System.out.println("Temps écoulé");
                result.cancel(true);
            }
        } finally {
            ex.shutdownNow();
        }
        return input;
    }
}
