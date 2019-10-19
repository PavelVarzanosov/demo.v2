package com.example.demo.model;

import com.example.demo.rateLimit.SimpleRateLimiter;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

public class PropListTest {

    private PropList propList;

    @Before
    public void setUp() throws Exception {
        propList = new PropList();
        int rateLimit = 100;
        SimpleRateLimiter simpleRateLimiter = SimpleRateLimiter.create(rateLimit, TimeUnit.MINUTES);
        Properties prop = new Properties("/newWidgetWithZIndex",rateLimit, simpleRateLimiter);
        Properties prop2 = new Properties("/newWidget",rateLimit, simpleRateLimiter);
        Properties prop3 = new Properties("/getWidget",rateLimit, simpleRateLimiter);
        propList.addProp(prop);
        propList.addProp(prop2);
        propList.addProp(prop3);
//        propList.add(prop);
//        propList.add(prop2);
//        propList.add(prop3);
    }

    @After
    public void tearDown() throws Exception {
        //propList.clear();
    }

    @Test
    public void addProp() {
        int rateLimit = 100;
        SimpleRateLimiter simpleRateLimiter = SimpleRateLimiter.create(rateLimit, TimeUnit.MINUTES);
        Properties prop = new Properties("/updateWidget",rateLimit, simpleRateLimiter);
        propList.addProp(prop);
        //Properties getprop
        //assertEquals(propList, getPropList);
    }

    @Test
    public void getProps() {
       // List<Properties> getPropList = propList.getProps();
        //assertEquals(propList, getPropList);
    }
}