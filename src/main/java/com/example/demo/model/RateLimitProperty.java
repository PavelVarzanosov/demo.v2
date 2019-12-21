package com.example.demo.model;
import com.example.demo.rateLimit.SimpleRateLimiter;

public class RateLimitProperty {
    private int rateLimit;
    private String path;
    private SimpleRateLimiter limiter;

    public RateLimitProperty(String path, int rateLimit, SimpleRateLimiter limiter) {
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

    public SimpleRateLimiter getRateLimiter() {
        return limiter;
    }

    public void setRateLimit(int value) {
        this.rateLimit = value;
    }
}
