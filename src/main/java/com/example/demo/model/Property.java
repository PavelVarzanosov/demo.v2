package com.example.demo.model;


import com.example.demo.rateLimit.SimpleRateLimiter;

public class Property {
    private int rateLimit;
    private String path;
    private SimpleRateLimiter limiter;
    public Property(String path, int rateLimit, SimpleRateLimiter limiter) {
        this.path = path;
        this.rateLimit=rateLimit;
        this.limiter=limiter;
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
}
