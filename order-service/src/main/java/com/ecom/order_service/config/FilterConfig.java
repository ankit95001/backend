package com.ecom.order_service.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean<JwtRequestFilter> jwtFilter(JwtRequestFilter filter) {
        FilterRegistrationBean<JwtRequestFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(filter);
        registrationBean.addUrlPatterns("/order/*"); // Apply filter to /product routes
        return registrationBean;
    }
}

