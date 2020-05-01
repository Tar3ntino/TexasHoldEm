package com.poker;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

public class ConsoleInputReadTask implements Callable<String> {

    private int period;
    private int timeout;

    public ConsoleInputReadTask(int timeout, int period) {
        this.timeout = timeout;
        this.period = period;
    }

    public String call() throws IOException {
        System.out.print(timeout + "s pour répondre... ");
        long beginTime = System.currentTimeMillis();
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String input;
        int periodReached = period;
        long timeElapsed;
        try {
            while (!br.ready()) {
                timeElapsed = System.currentTimeMillis() - beginTime;
                if (timeElapsed >= TimeUnit.MILLISECONDS.convert(periodReached, TimeUnit.SECONDS)) {
                    int nbSecondsRemaining = timeout - periodReached;
                    System.out.print(nbSecondsRemaining + "s ... ");
                    periodReached += period;
                }
                Thread.sleep(200);
            }
            input = br.readLine();
        } catch (InterruptedException e) {
            return null;
        }
        return input;
    }
}
