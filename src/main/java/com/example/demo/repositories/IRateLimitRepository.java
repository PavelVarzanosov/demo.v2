package com.example.demo.repositories;

import com.example.demo.model.RateLimitProperty;
import com.example.demo.model.Widget;
import javassist.NotFoundException;

import java.util.List;
import java.util.UUID;

public interface IRateLimitRepository {

    void addProperty(RateLimitProperty prop);

    void updateProperty(RateLimitProperty prop) throws NotFoundException;

    List<RateLimitProperty> getProperties();

    void deleteAll();

    RateLimitProperty getByPath(String path) throws NotFoundException;
}
