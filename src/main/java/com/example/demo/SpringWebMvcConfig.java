package com.example.demo;
import com.example.demo.rateLimit.RateLimitingInterceptor;
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

    @Bean
    RateLimitingInterceptor RateLimitInterceptor() {
        return new RateLimitingInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(RateLimitInterceptor());
    }
}