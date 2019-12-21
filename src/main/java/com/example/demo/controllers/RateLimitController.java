package com.example.demo.controllers;

import com.example.demo.services.rateLimitService.IRateLimitService;
import com.example.demo.services.rateLimitService.rateLimitServiceImpl.ListRateLimitServiceImpl;
import com.example.demo.model.RateLimitProperty;
import com.example.demo.rateLimit.SimpleRateLimiter;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/rateLimit")
public class RateLimitController {

    @Autowired
    private IRateLimitService rateLimitService;

    public RateLimitController() {

    }

    @PostMapping("/addOrUpdateRateLimit")
    public ResponseEntity<RateLimitProperty> addOrUpdateRateLimit(@RequestParam String contextPath, int rateLimit ) {
        SimpleRateLimiter rateLimiter = SimpleRateLimiter.create(rateLimit, TimeUnit.MINUTES);
        RateLimitProperty newProperty = new RateLimitProperty(contextPath, rateLimit, rateLimiter);

        try {
            rateLimitService.addOrUpdateProperty(newProperty);
            return ResponseEntity.ok(newProperty);
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/getRateLimits")
    public ResponseEntity<List<RateLimitProperty>> getProps() {
        List<RateLimitProperty> properties = rateLimitService.getProperties();
        return ResponseEntity.ok(properties);
    }
}
