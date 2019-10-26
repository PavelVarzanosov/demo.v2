package com.example.demo.controllers;

import com.example.demo.model.PropertiesList;
import com.example.demo.model.Property;
import com.example.demo.rateLimit.SimpleRateLimiter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/rateLimit")
public class RateLimitController {
    private PropertiesList propertiesList = new PropertiesList();;
    public RateLimitController() {

    }

    @PostMapping("/addOrUpdateRateLimit")
    public ResponseEntity<Property> addOrUpdateRateLimit(@RequestParam String contextPath, int rateLimit ) {
        Optional<Property> property = propertiesList.getProperties().stream().filter((w) -> w.getPath().equals(contextPath)).findAny();

        if(property.isPresent())
        {
            property.get().setRateLimit(rateLimit);
            return ResponseEntity.noContent().build();
        } else
        {
            SimpleRateLimiter rateLimiter = SimpleRateLimiter.create(rateLimit, TimeUnit.MINUTES);
            Property newProperty = new Property(contextPath, rateLimit, rateLimiter);
            propertiesList.addProperty(newProperty);
            return ResponseEntity.ok(newProperty);
        }
    }

    @GetMapping("/getRateLimits")
    public ResponseEntity<List<Property>> getProps() {
        List<Property> properties = propertiesList.getProperties();
        return ResponseEntity.ok(properties);
    }
}
