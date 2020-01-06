package com.example.demo.services.rateLimitService.rateLimitServiceImpl;

import com.example.demo.model.RateLimitProperty;
import com.example.demo.repositories.IRateLimitRepository;
import com.example.demo.services.IRateLimitService;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

public class ListRateLimitServiceImpl implements IRateLimitService {

    @Autowired
    private IRateLimitRepository rateLimitRepository;
    private final Object lock = new Object();

    public ListRateLimitServiceImpl(){

    }

    public void addOrUpdateProperty(RateLimitProperty prop) throws NotFoundException {
        RateLimitProperty rateLimitProperty = null;
        try {
            rateLimitProperty = rateLimitRepository.getByPath(prop.getPath());
            rateLimitRepository.addProperty(prop);
        } catch (NotFoundException e) {
            synchronized (lock) {
                rateLimitRepository.updateProperty(prop);
            }
        }
    }

    public List<RateLimitProperty> getProperties(){
        return rateLimitRepository.getProperties();
    }
}
