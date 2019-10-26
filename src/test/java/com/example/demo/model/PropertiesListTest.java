package com.example.demo.model;

import com.example.demo.rateLimit.SimpleRateLimiter;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

public class PropertiesListTest {

    private PropertiesList propertiesList;

    @Before
    public void setUp() throws Exception {
        propertiesList = new PropertiesList();
        int rateLimit = 100;
        SimpleRateLimiter simpleRateLimiter = SimpleRateLimiter.create(rateLimit, TimeUnit.MINUTES);
        Property prop = new Property("/newWidgetWithZIndex",rateLimit, simpleRateLimiter);
        Property prop2 = new Property("/newWidget",rateLimit, simpleRateLimiter);
        Property prop3 = new Property("/getWidget",rateLimit, simpleRateLimiter);
        propertiesList.addProperty(prop);
        propertiesList.addProperty(prop2);
        propertiesList.addProperty(prop3);
    }

    @After
    public void tearDown() throws Exception {
        propertiesList = new PropertiesList();
    }

    @Test
    public void addProp() {
        int rateLimit = 100;
        SimpleRateLimiter simpleRateLimiter = SimpleRateLimiter.create(rateLimit, TimeUnit.MINUTES);
        Property property = new Property("/updateWidget",rateLimit, simpleRateLimiter);
        propertiesList.addProperty(property);
        Property getProperty = propertiesList.getProperties().stream().filter((prop) ->prop.getPath().equals("/updateWidget")).findAny().get();
        Assert.assertEquals(property, getProperty);
    }

    @Test
    public void getProps() {
       // List<Properties> getPropList = propList.getProps();
        //assertEquals(propList, getPropList);
    }
}