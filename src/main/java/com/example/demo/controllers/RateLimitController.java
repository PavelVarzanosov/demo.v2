package com.example.demo.controllers;

import com.example.demo.model.PropList;
import com.example.demo.model.Properties;
import com.example.demo.model.Widget;
import com.example.demo.rateLimit.SimpleRateLimiter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.TimeUnit;
@RestController
@RequestMapping("/rateLimit")
public class RateLimitController {
    private PropList propList = new PropList();;
    public RateLimitController() {

    }

    @PostMapping("/addOrUpdateRateLimit")
    public ResponseEntity<Properties> addOrUpdateRateLimit(@RequestParam String contextPath, int rateLimit ) {

        if(propList.getProps()
                .stream()
                .filter((w) -> w.getPath().equals(contextPath))
                .findAny()
                .isPresent())
        {
            propList.getProps()
                    .stream()
                    .filter((w) -> w.getPath().equals(contextPath))
                    .findAny()
                    .get()
                    .setRateLimit(rateLimit);
            return ResponseEntity.noContent().build();
        } else
        {
            SimpleRateLimiter rateLimiter = SimpleRateLimiter.create(rateLimit, TimeUnit.MINUTES);
            Properties prop = new Properties(contextPath, rateLimit, rateLimiter);
            propList.addProp(prop);
            return ResponseEntity.ok(prop);
        }
    }

    @GetMapping("/getRateLimits")
    public ResponseEntity<List<Properties>> getProps() {

        List<Properties> props = propList.getProps();
        return ResponseEntity.ok(props);
    }
}
