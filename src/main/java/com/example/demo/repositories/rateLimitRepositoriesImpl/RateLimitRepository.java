package com.example.demo.repositories.rateLimitRepositoriesImpl;

import com.example.demo.model.RateLimitProperty;
import com.example.demo.repositories.IRateLimitRepository;
import javassist.NotFoundException;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

public class RateLimitRepository implements IRateLimitRepository {

    private static List<RateLimitProperty> propertyList = new CopyOnWriteArrayList<>();

    public void addProperty(RateLimitProperty prop) {
        propertyList.add(prop);
    }

    public synchronized void updateProperty(RateLimitProperty prop) throws NotFoundException {
        Optional<RateLimitProperty> property = propertyList.stream().filter((p) -> p.getPath().equals(prop.getPath())).findFirst();
        if(property.isPresent())
        {
            propertyList.stream().filter((p) -> p.getPath().equals(prop.getPath())).findFirst().get().setRateLimit(prop.getRateLimit());
        } else {
            throw new NotFoundException("RateLimitProperty with path " + prop.getPath()+ " not found");
        }
    }

    public List<RateLimitProperty> getProperties() {
        return propertyList;
    }

    public RateLimitProperty getByPath(String path) throws NotFoundException{
        Optional<RateLimitProperty> rateLimitProperty = propertyList.stream()
                .filter((rateLimit) -> rateLimit.getPath().equals(path))
                .findAny();
            if(rateLimitProperty.isPresent()){
                return rateLimitProperty.get();
            } else {
                throw new NotFoundException("RateLimitProperty with path " + path + " not found");
            }
    }

    public void deleteAll() {
        propertyList = new CopyOnWriteArrayList<>();
    }
}
