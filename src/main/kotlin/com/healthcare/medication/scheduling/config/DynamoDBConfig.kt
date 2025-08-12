package com.healthcare.medication.scheduling.config

import io.micronaut.context.annotation.Bean
import io.micronaut.context.annotation.Factory
import io.micronaut.context.annotation.Requires
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.dynamodb.DynamoDbClient
import jakarta.inject.Singleton
import java.net.URI

@Factory
@Requires(notEnv = ["test"])
class DynamoDBConfig {

    @Bean
    @Singleton
    @MedicationSchedulingDynamoDb
    fun dynamoDbClient(): DynamoDbClient {
        return DynamoDbClient.builder()
            .region(Region.US_EAST_1)
            .endpointOverride(URI.create("http://localhost:8000")) // For local DynamoDB
            .build()
    }

    @Bean
    @Singleton
    @MedicationSchedulingDynamoDb
    fun dynamoDbEnhancedClient(@MedicationSchedulingDynamoDb dynamoDbClient: DynamoDbClient): DynamoDbEnhancedClient {
        return DynamoDbEnhancedClient.builder()
            .dynamoDbClient(dynamoDbClient)
            .build()
    }
}