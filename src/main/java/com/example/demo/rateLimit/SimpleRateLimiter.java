package com.example.demo.rateLimit;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class SimpleRateLimiter {
    private Semaphore semaphore;
    private int maxPermits;
    private TimeUnit timePeriod;//час, день или минута
    private ScheduledExecutorService scheduler;
    private long startSchedulerTime;
    SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
    public static SimpleRateLimiter create(int permits, TimeUnit timePeriod) {
        SimpleRateLimiter limiter = new SimpleRateLimiter(permits, timePeriod);
        limiter.schedulePermitReplenishment();
        return limiter;
    }

    private SimpleRateLimiter(int permits, TimeUnit timePeriod) {
        this.semaphore = new Semaphore(permits);
        this.maxPermits = permits;
        this.timePeriod = timePeriod;
    }

    public boolean tryAcquire() {
        return semaphore.tryAcquire();
    }

    public void stop() {
        scheduler.shutdownNow();
    }

    public int getLeftLimit() {
        return semaphore.availablePermits();
    }

    public String getNextResetDate() {
        return format.format(new Date(startSchedulerTime + timePeriod.toMillis(1)));
    }

    public void schedulePermitReplenishment() {
        scheduler = Executors.newScheduledThreadPool(1);
        startSchedulerTime = System.currentTimeMillis();
        scheduler.scheduleAtFixedRate(() -> {
            semaphore.release(maxPermits - semaphore.availablePermits());
            startSchedulerTime = System.currentTimeMillis();
        }, 1,1, timePeriod);

    }
}