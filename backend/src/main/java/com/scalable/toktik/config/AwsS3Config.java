package com.scalable.toktik.config;


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
public class AwsS3Config {
    @Value("${aws.accessKey}")
    private String accessKey;

    @Value("${aws.secretKey}")
    private String secretKey;

    @Value("${aws.endpoint}")
    private String endpoint;

    @Value("${aws.region}")
    private String region;

    @Bean
    public AmazonS3 getAmazonS3Client() {
        final BasicAWSCredentials basicAwsCredentials = new BasicAWSCredentials(accessKey, secretKey);
        // Get Amazon S3 client and return the S3 client object
        return AmazonS3ClientBuilder
                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(basicAwsCredentials))
                .withRegion(endpoint)
                .build();
    }

    @Bean
    public AmazonS3 linodeClient() {
        AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
        return AmazonS3ClientBuilder
                .standard()
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(
                        "https://" + endpoint, region))
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .build();
    }
}