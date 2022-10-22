package com.beside.special.config;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ImageUploadConfig {

    private final ImageConfigProperties imageConfigProperties;

    @Autowired
    public ImageUploadConfig(ImageConfigProperties imageConfigProperties) {
        this.imageConfigProperties = imageConfigProperties;
    }

    @Bean
    public AmazonS3 amazonS3() {
        AWSCredentials awsCredentials = new BasicAWSCredentials(
                imageConfigProperties.getAccessKey(), imageConfigProperties.getSecretKey()
        );

        return AmazonS3ClientBuilder.standard()
                .withEndpointConfiguration(
                        new AwsClientBuilder.EndpointConfiguration(
                                imageConfigProperties.getEndPoint(), imageConfigProperties.getRegionName()))
                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                .build();
    }
}
