package com.example.demo;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@ConfigurationProperties(prefix="spring")
@Validated
public class Properties {
    private boolean isInMemoryStorage;
    private int rateLimitForSave;
    private int rateLimitForUpdate;
    private int rateLimitForDelete;
    private int rateLimitForGetList;
    public boolean getIsInMemoryStorage() {
        return isInMemoryStorage;
    }
    public int getRateLimitForSave() {
        return rateLimitForSave;
    }
    public int getRateLimitForUpdate() {
        return rateLimitForUpdate;
    }
    public int getRateLimitForDelete() {
        return rateLimitForDelete;
    }
    public int getRateLimitForGetList() {
        return rateLimitForGetList;
    }
    public void setRateLimitForSave(int value) {
        this.rateLimitForSave = value;
    }
    public void setRateLimitForUpdate(int value) {
        this.rateLimitForUpdate = value;
    }
    public void setRateLimitForDelete(int value) {
        this.rateLimitForDelete = value;
    }
    public void setRateLimitForGetList(int value) {
        this.rateLimitForGetList = value;
    }
}
