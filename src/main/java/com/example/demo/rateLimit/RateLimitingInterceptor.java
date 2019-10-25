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
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Component
public class RateLimitingInterceptor extends HandlerInterceptorAdapter {

    @Value("${spring.baseRateLimit}")
    private int limit;

    private PropList propList = new PropList();
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String path = request.getServletPath();
        if (path == null) {
            return true;
        }
        Optional<Properties> propOrNull = propList.getProps().stream().filter((w) -> w.getPath().equals(path)).findAny();
        SimpleRateLimiter limiter = getRateLimiter(path, propOrNull, limit);
        boolean allowRequest = limiter.tryAcquire();

        if (!allowRequest) {
            response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
        }
        int limitForPath = (propOrNull.isPresent()) ? propOrNull.get().getRateLimit(): limit;
        response.addHeader("X-RateLimit-Limit", String.valueOf(limitForPath));
        response.addHeader("left-RateLimit-Limit", String.valueOf(limiter.getLeftLimit()));
        response.addHeader("next-Reset-Date", limiter.getNextResetDate());
        return allowRequest;
    }
    private SimpleRateLimiter getRateLimiter(String path, Optional<Properties> propOrNull, int limitForPath) {
        //Optional<Properties> propOrNull = propList.getProps().stream().filter((w) -> w.getPath().equals(path)).findAny();
        if (propOrNull.isPresent()) {
            return propOrNull.get().getRateLimiter();
        } else {
            //synchronized(path.intern()) {
            if (propOrNull.isPresent()) {
                return propOrNull.get().getRateLimiter();
            } else  {
                //если в списке проперти нет такого, то просто отдадим базовый и добавим в список;
                SimpleRateLimiter rateLimiter = SimpleRateLimiter.create(limitForPath, TimeUnit.MINUTES);
                Properties prop = new Properties(path, limitForPath, rateLimiter);
                propList.addProp(prop);
                return rateLimiter;
            }
            //}
        }
    }

    @PreDestroy
    public void destroy() {
    }
}
