package com.example.demo.controllers;

import com.example.demo.Properties;
import com.example.demo.Widget;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class RateLimitController {
    private final String sharedKey = "SHARED_KEY";

    public RateLimitController() {
    }

    @PostMapping("/updateRateLimit")
    public void updateRateLimit(@RequestBody String type, int rateLimit ) {
        Properties prop = new Properties();
        switch(type) {
            case "rateLimitForSave":
                prop.setRateLimitForSave(rateLimit);
                break;
            case "rateLimitForUpdate":
                prop.setRateLimitForUpdate(rateLimit);
                break;
            case "rateLimitForDelete":
                prop.setRateLimitForDelete(rateLimit);
                break;
            case "rateLimitForGetList":
                prop.setRateLimitForGetList(rateLimit);
                break;
            default: break;
        }
    }

}
