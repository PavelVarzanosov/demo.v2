package com.example.demo.model;


import com.example.demo.rateLimit.SimpleRateLimiter;

public class Properties {
    private int rateLimit;
    private String path;
    private SimpleRateLimiter limiter;
    public Properties (String path, int rateLimit,SimpleRateLimiter limiter) {
        this.path = path;
        this.rateLimit=rateLimit;
        this.limiter=limiter;
    }
    public Properties () {
    }
    public int getRateLimit() {
        return rateLimit;
    }
    public String getPath() {
        return path;
    }
    public void setRateLimit(int value) {
        this.rateLimit = value;
    }
    public SimpleRateLimiter getRateLimiter() {
        return limiter;
    }
    public void setRateLimiter(SimpleRateLimiter limiter) {
        this.limiter = limiter;
    }
}
