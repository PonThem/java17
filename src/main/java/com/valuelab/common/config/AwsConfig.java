package com.valuelab.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProvider;
import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProviderClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

@Configuration
public class AwsConfig {
    @Value("${cloud.aws.region.static}")
    private String region;

    @Bean
    public AWSCognitoIdentityProvider awsCognitoIdentityProviderAdmin() {
        return AWSCognitoIdentityProviderClientBuilder.standard()
                .withRegion(region)
                .build();
    }

    // Bean for Cognito Identity Provider using default credentials
    @Bean
    public AWSCognitoIdentityProvider awsCognitoIdentityProviderBasic() {
        return AWSCognitoIdentityProviderClientBuilder.standard()
                .withRegion(region)
                .build();
    }

    @Bean
    public AmazonS3 amazonS3() {

        return AmazonS3ClientBuilder.standard()
                .withRegion(region) // Specify your region
                .build();
    }
}
