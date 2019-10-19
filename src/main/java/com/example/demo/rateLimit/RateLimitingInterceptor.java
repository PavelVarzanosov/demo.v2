package com.example.demo.rateLimit;

import com.example.demo.model.PropList;
import com.example.demo.model.Properties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.annotation.PreDestroy;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Component
public class RateLimitingInterceptor extends HandlerInterceptorAdapter {

//    @Value("${spring.limitEnabled}")
//    private boolean enabled;
//
    @Value("${spring.baseRateLimit}")
    private int limit;

    private PropList propList = new PropList();
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
//        if (!enabled) {
//            return true;
//        }
        //сделать разделение  по запросам
        String path = request.getServletPath();
        if (path == null) {
            return true;
        }
        System.out.println("request " + request.getServletPath());
        SimpleRateLimiter limiter = getRateLimiter(path);
        boolean allowRequest = limiter.tryAcquire();

        if (!allowRequest) {
            response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
        }
        int limitForPath = limit;
        if(propList.getProps().stream().filter((w) -> w.getPath().equals(path)).findAny().isPresent()) {
            //здесь запросить проперти и определить сколько для этого запроса  разрешено
            limitForPath = propList.getProps()
                    .stream()
                    .filter((w) -> w.getPath().equals(path))
                    .findAny()
                    .get()
                    .getRateLimit();
        }
        response.addHeader("X-RateLimit-Limit", String.valueOf(limitForPath));
        response.addHeader("left-RateLimit-Limit", String.valueOf(limiter.getLeftLimit()));
        response.addHeader("next-Reset-Date", limiter.getNextResetDate());
        return allowRequest;
    }
//получим нужный simpleRateLimiter из списка
    private SimpleRateLimiter getRateLimiter(String path) {
        if (propList.getProps()
                .stream()
                .filter((w) -> w.getPath().equals(path))
                .findAny()
                .isPresent()) {
            return propList.getProps()
                    .stream()
                    .filter((w) -> w.getPath().equals(path))
                    .findAny()
                    .get()
                    .getRateLimiter();
        } else {
            synchronized(path.intern()) {
                // double-checked locking to avoid multiple-reinitializations
                if (propList.getProps()
                        .stream()
                        .filter((w) -> w.getPath().equals(path))
                        .findAny()
                        .isPresent()) {
                    return propList.getProps()
                            .stream()
                            .filter((w) -> w.getPath().equals(path))
                            .findAny()
                            .get()
                            .getRateLimiter();
                } else  {
                    //если в списке проперти нет такого, то просто отдадим базовый и добавим в список;
                    SimpleRateLimiter rateLimiter = SimpleRateLimiter.create(limit, TimeUnit.MINUTES);
                    Properties prop = new Properties(path, limit, rateLimiter);
                    propList.addProp(prop);
                    return rateLimiter;
                }

            }
        }
    }

    @PreDestroy
    public void destroy() {
        // loop and finalize all limiters
    }
}
