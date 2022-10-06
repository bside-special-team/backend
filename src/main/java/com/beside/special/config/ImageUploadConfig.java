package com.beside.special.config;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ImageUploadConfig {
    @Value("${ncp.accessKey}")
    private String accessKey;

    @Value("${ncp.secretKey}")
    private String secretKey;

    @Value("${ncp.endPoint}")
    private String endPoint;

    @Value("${ncp.regionName}")
    private String regionName;

    @Bean
    public AmazonS3 amazonS3() {
        AWSCredentials awsCredentials = new BasicAWSCredentials(accessKey, secretKey);
        return AmazonS3ClientBuilder.standard()
                .withEndpointConfiguration(
                        new AwsClientBuilder.EndpointConfiguration(endPoint, regionName))
                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                .build();
    }
}
