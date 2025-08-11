package com.healthcare.medication.scheduling.config

import io.micronaut.context.annotation.Bean
import io.micronaut.context.annotation.Factory
import io.micronaut.context.annotation.Value
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.dynamodb.DynamoDbClient
import jakarta.inject.Singleton
import java.net.URI

@Factory
class DynamoDBConfig {

    @Bean
    @Singleton
    fun dynamoDbClient(
        @Value("\${aws.region}") region: String,
        @Value("\${aws.dynamodb.endpoint}") endpoint: String
    ): DynamoDbClient {
        return DynamoDbClient.builder()
            .region(Region.of(region))
            .endpointOverride(URI.create(endpoint))
            .credentialsProvider(
                StaticCredentialsProvider.create(
                    AwsBasicCredentials.create("dummy", "dummy")
                )
            )
            .build()
    }

    @Bean
    @Singleton
    fun dynamoDbEnhancedClient(dynamoDbClient: DynamoDbClient): DynamoDbEnhancedClient {
        return DynamoDbEnhancedClient.builder()
            .dynamoDbClient(dynamoDbClient)
            .build()
    }
}