package com.example.demo.repositories.rateLimitRepositoriesImpl;

import com.example.demo.model.RateLimitProperty;
import com.example.demo.rateLimit.SimpleRateLimiter;
import com.example.demo.repositories.IRateLimitRepository;
import javassist.NotFoundException;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.TimeUnit;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RateLimitRepositoryTest {

    @Autowired
    private IRateLimitRepository rateLimitRepository;

    private RateLimitProperty prop;
    private RateLimitProperty prop2;
    private RateLimitProperty prop3;

    @Before
    public void setUp() {
        int rateLimit = 100;
        SimpleRateLimiter simpleRateLimiter = SimpleRateLimiter.create(rateLimit, TimeUnit.MINUTES);
        prop = new RateLimitProperty("/newWidgetWithZIndex",rateLimit, simpleRateLimiter);
        prop2 = new RateLimitProperty("/newWidget",rateLimit, simpleRateLimiter);
        prop3 = new RateLimitProperty("/updateWidget",rateLimit, simpleRateLimiter);
        rateLimitRepository.addProperty(prop);
        rateLimitRepository.addProperty(prop2);
        rateLimitRepository.addProperty(prop3);
    }

    @After
    public void tearDown() {
        rateLimitRepository.deleteAll();
    }

    @Test
    public void testUpdateProperty() throws NotFoundException {
        int rateLimit = 150;
        SimpleRateLimiter simpleRateLimiter = SimpleRateLimiter.create(rateLimit, TimeUnit.MINUTES);
        RateLimitProperty property = new RateLimitProperty("/updateWidget",rateLimit, simpleRateLimiter);

        rateLimitRepository.updateProperty(property);
        RateLimitProperty getProperty = rateLimitRepository.getProperties().stream().filter((prop) ->prop.getPath().equals("/updateWidget")).findAny().get();

        Assert.assertEquals(rateLimit, getProperty.getRateLimit());
    }

    @Test
    public void testUpdatePropertyWithException() {
        int rateLimit = 150;
        SimpleRateLimiter simpleRateLimiter = SimpleRateLimiter.create(rateLimit, TimeUnit.MINUTES);
        RateLimitProperty property = new RateLimitProperty("/updateWidget",rateLimit, simpleRateLimiter);
        try {
            rateLimitRepository.updateProperty(property);
        } catch (NotFoundException e) {
            Assert.assertEquals("RateLimitProperty with path " + "/updateWidget" + " not found", e.getMessage());
        }
    }

    @Test
    public void testGetByPathProperty() throws NotFoundException {
        RateLimitProperty property = rateLimitRepository.getByPath("/newWidget");

        Assert.assertEquals(prop2.getRateLimit(), property.getRateLimit());
    }

    @Test
    public void testGetByPathPropertyWithException() {
        try {
            rateLimitRepository.getByPath("/updateWidget");
        } catch (NotFoundException e) {
            Assert.assertEquals("RateLimitProperty with path " + "/updateWidget" + " not found", e.getMessage());
        }
    }

}