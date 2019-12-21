package com.example.demo.rateLimit;

import com.example.demo.repositories.IRateLimitRepository;
import com.example.demo.services.rateLimitService.rateLimitServiceImpl.ListRateLimitServiceImpl;
import com.example.demo.model.RateLimitProperty;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private IRateLimitRepository rateLimitRepository;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String path = request.getServletPath();
        if (path == null) {
            return true;
        }

        Optional<RateLimitProperty> property = rateLimitRepository.getProperties().stream().filter((p) -> p.getPath().equals(path)).findAny();
        SimpleRateLimiter limiter = getRateLimiter(path, property, limit);
        boolean allowRequest = limiter.tryAcquire();
        if (!allowRequest) {
            response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
        }
        int limitForPath = property.map(RateLimitProperty::getRateLimit).orElseGet(() -> limit);

        response.addHeader("X-RateLimit-Limit", String.valueOf(limitForPath));
        response.addHeader("left-RateLimit-Limit", String.valueOf(limiter.getLeftLimit()));
        response.addHeader("next-Reset-Date", limiter.getNextResetDate());
        return allowRequest;
    }

    private SimpleRateLimiter getRateLimiter(String path, Optional<RateLimitProperty> property, int limitForPath) {
        if (property.isPresent()) {
            return property.get().getRateLimiter();
        } else {
            synchronized(path.intern()) {
                if (property.isPresent()) {
                    return property.get().getRateLimiter();
                } else  {
                    SimpleRateLimiter rateLimiter = SimpleRateLimiter.create(limitForPath, TimeUnit.MINUTES);
                    RateLimitProperty newProperty = new RateLimitProperty(path, limitForPath, rateLimiter);
                    rateLimitRepository.addProperty(newProperty);
                    return rateLimiter;
                }
            }
        }
    }

    @PreDestroy
    public void destroy() {
    }
}
