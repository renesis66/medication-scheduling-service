plugins {
    id("org.jetbrains.kotlin.jvm") version "1.9.20"
    id("org.jetbrains.kotlin.plugin.allopen") version "1.9.20"
    id("com.github.johnrengelman.shadow") version "8.1.1"
    id("io.micronaut.application") version "4.2.1"
    id("io.micronaut.test-resources") version "4.2.1"
}

version = "0.1"
group = "com.medscheduler"

val kotlinVersion = project.properties.get("kotlinVersion")
repositories {
    mavenCentral()
}

dependencies {
    // kapt("io.micronaut:micronaut-http-validation")
    // kapt("io.micronaut.serde:micronaut-serde-processor")
    
    implementation("io.micronaut:micronaut-http-client")
    implementation("io.micronaut:micronaut-jackson-databind")
    implementation("io.micronaut.kotlin:micronaut-kotlin-runtime")
    implementation("io.micronaut.serde:micronaut-serde-jackson")
    implementation("io.micronaut.validation:micronaut-validation")
    implementation("org.jetbrains.kotlin:kotlin-reflect:${kotlinVersion}")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:${kotlinVersion}")
    
    // AWS DynamoDB
    implementation("software.amazon.awssdk:dynamodb:2.21.29")
    implementation("software.amazon.awssdk:dynamodb-enhanced:2.21.29")
    
    // Mocking for tests
    testImplementation("io.mockk:mockk:1.13.8")
    
    // Date/Time
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.4.1")
    
    // Configuration support
    runtimeOnly("org.yaml:snakeyaml")
    
    // Logging
    runtimeOnly("ch.qos.logback:logback-classic")
    
    // Test dependencies
    testImplementation("io.micronaut:micronaut-http-client")
    testImplementation("io.micronaut.test:micronaut-test-junit5")
    testImplementation("org.junit.jupiter:junit-jupiter-api")
    testImplementation("org.junit.jupiter:junit-jupiter-engine")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test")
}

application {
    mainClass.set("com.medscheduler.ApplicationKt")
}

java {
    sourceCompatibility = JavaVersion.toVersion("17")
}

tasks {
    compileKotlin {
        kotlinOptions {
            jvmTarget = "17"
        }
    }
    compileTestKotlin {
        kotlinOptions {
            jvmTarget = "17"
        }
    }
}

graalvmNative.toolchainDetection = false
micronaut {
    runtime("netty")
    testRuntime("junit5")
}