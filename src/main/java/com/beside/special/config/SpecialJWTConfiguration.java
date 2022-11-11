package com.beside.special.config;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@Slf4j
@Getter
@ConstructorBinding
@ConfigurationProperties("special.jwt")
public class SpecialJWTConfiguration implements InitializingBean {
    private final String secret;

    public SpecialJWTConfiguration(String secret) {
        this.secret = secret;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("secret is {}", secret);
    }
}
