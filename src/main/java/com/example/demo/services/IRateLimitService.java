package com.example.demo.services;

import com.example.demo.model.RateLimitProperty;
import javassist.NotFoundException;

import java.util.List;

public interface IRateLimitService {

    void addOrUpdateProperty(RateLimitProperty prop) throws NotFoundException;

    List<RateLimitProperty> getProperties();
}
