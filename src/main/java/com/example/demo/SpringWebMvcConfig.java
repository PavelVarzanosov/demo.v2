package com.example.demo;
import com.example.demo.rateLimit.RateLimitingInterceptor;
import com.example.demo.repositories.IRateLimitRepository;
import com.example.demo.repositories.rateLimitRepositoriesImpl.RateLimitRepository;
import com.example.demo.repositories.widgetRepositoriesImpl.H2WidgetRepository;
import com.example.demo.repositories.IWidgetRepository;
import com.example.demo.repositories.widgetRepositoriesImpl.ListWidgetRepository;
import com.example.demo.services.IRateLimitService;
import com.example.demo.services.rateLimitService.rateLimitServiceImpl.ListRateLimitServiceImpl;
import com.example.demo.services.IWidgetService;
import com.example.demo.services.widgetService.widgetServiceImpl.WidgetServiceImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@EnableWebMvc
@Configuration
@ComponentScan({"com.example.demo"})
public class SpringWebMvcConfig extends WebMvcConfigurerAdapter {

    @Value("${spring.isInMemoryStorage}") String isInMemoryStorage;

    @Bean
    public IWidgetService getWidgetService() {
        return new WidgetServiceImpl();
    }

    @Bean
    public IRateLimitService getRateLimitService() {
        return new ListRateLimitServiceImpl();
    }

    @Bean
    public IRateLimitRepository getRateLimitRepository() {
        return new RateLimitRepository();
    }

    @Bean
    public IWidgetRepository getWidgetRepository() {
        if (isInMemoryStorage.equals("true")) return new H2WidgetRepository();
        else return new ListWidgetRepository();
    }

    @Bean
    RateLimitingInterceptor RateLimitInterceptor() {
        return new RateLimitingInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(RateLimitInterceptor());
    }
}