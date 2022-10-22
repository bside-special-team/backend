package com.beside.special.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@Getter
@RequiredArgsConstructor
@ConstructorBinding
@ConfigurationProperties("ncp")
public class ImageConfigProperties {
    private final String accessKey;
    private final String secretKey;
    private final String endPoint;
    private final String regionName;
}