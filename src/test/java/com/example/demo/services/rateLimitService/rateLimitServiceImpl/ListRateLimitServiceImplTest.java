package com.example.demo.services.rateLimitService.rateLimitServiceImpl;

import com.example.demo.model.RateLimitProperty;
import com.example.demo.rateLimit.SimpleRateLimiter;
import com.example.demo.repositories.IRateLimitRepository;
import javassist.NotFoundException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.TimeUnit;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ListRateLimitServiceImplTest {

    @Mock
    private IRateLimitRepository rateLimitRepositoryMock;

    @InjectMocks
    private ListRateLimitServiceImpl listRateLimitService;

    private RateLimitProperty prop;
    private RateLimitProperty prop2;
    private RateLimitProperty prop3;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        int rateLimit = 100;
        SimpleRateLimiter simpleRateLimiter = SimpleRateLimiter.create(rateLimit, TimeUnit.MINUTES);
        prop = new RateLimitProperty("/newWidgetWithZIndex",rateLimit, simpleRateLimiter);
        prop2 = new RateLimitProperty("/newWidget",rateLimit, simpleRateLimiter);
        prop3 = new RateLimitProperty("/updateWidget",rateLimit, simpleRateLimiter);
    }

    @After
    public void tearDown() {
    }

    @Test
    public void listTestAddProperty() throws NotFoundException {
        int rateLimit = 150;
        SimpleRateLimiter simpleRateLimiter = SimpleRateLimiter.create(rateLimit, TimeUnit.MINUTES);
        RateLimitProperty property = new RateLimitProperty("/getWidget",rateLimit, simpleRateLimiter);
        doReturn(property).when(rateLimitRepositoryMock).getByPath("/getWidget");
        doNothing().when(rateLimitRepositoryMock).addProperty(property);

        listRateLimitService.addOrUpdateProperty(property);

        verify(rateLimitRepositoryMock, times(1)).addProperty(property);
        verify(rateLimitRepositoryMock, times(1)).getByPath("/getWidget");
        verifyNoMoreInteractions(rateLimitRepositoryMock);
    }

    @Test
    public void listTestUpdateProperty() throws NotFoundException {
        int rateLimit = 150;
        SimpleRateLimiter simpleRateLimiter = SimpleRateLimiter.create(rateLimit, TimeUnit.MINUTES);
        RateLimitProperty property = new RateLimitProperty("/newWidget",rateLimit, simpleRateLimiter);
        doThrow(new NotFoundException("RateLimitProperty with path " + property.getPath() + " not found")).when(rateLimitRepositoryMock).getByPath("/newWidget");
        doNothing().when(rateLimitRepositoryMock).updateProperty(property);

        listRateLimitService.addOrUpdateProperty(property);

        verify(rateLimitRepositoryMock, times(1)).updateProperty(property);
        verify(rateLimitRepositoryMock, times(1)).getByPath("/newWidget");
        verifyNoMoreInteractions(rateLimitRepositoryMock);
    }
}