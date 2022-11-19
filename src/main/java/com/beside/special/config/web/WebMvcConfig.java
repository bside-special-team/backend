package com.beside.special.config.web;

import com.beside.special.config.SpecialJWTConfiguration;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    private final UserArgumentResolver userArgumentResolver;

    public WebMvcConfig(UserArgumentResolver userArgumentResolver) {
        this.userArgumentResolver = userArgumentResolver;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(userArgumentResolver);
    }

    @Bean
    public FilterRegistrationBean filterBean(SpecialJWTConfiguration specialJWTConfiguration,
                                             ObjectMapper objectMapper) {
        FilterRegistrationBean registrationBean =
            new FilterRegistrationBean(new AuthorizationFilter(specialJWTConfiguration, objectMapper));
        registrationBean.addUrlPatterns("/api/*");
        return registrationBean;
    }

}
