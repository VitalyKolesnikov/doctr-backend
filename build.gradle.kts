plugins {
    id("org.springframework.boot") version "3.3.0"
    id("io.spring.dependency-management") version "1.1.5"
    java
    jacoco
}

group = "ru.kvs"
version = "0.0.1-SNAPSHOT"
description = "doctr-backend"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

val mapstructVersion = "1.5.3.Final"
val lombokMapstructBindingVersion = "0.2.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.org.springframework.boot.spring.boot.starter.data.jpa)
    implementation(libs.org.springframework.boot.spring.boot.starter.security)
    implementation(libs.org.springframework.boot.spring.boot.starter.web)
    implementation(libs.org.springframework.boot.spring.boot.starter.actuator)

    runtimeOnly(libs.org.postgresql.postgresql)

    implementation(libs.org.flywaydb.flyway.core)
    implementation(libs.org.hibernate.validator.hibernate.validator)
    implementation(libs.io.jsonwebtoken.jjwt)
    implementation(libs.org.mapstruct.mapstruct)
    implementation(libs.org.projectlombok.lombok.mapstruct.binding)
    implementation(libs.org.springdoc.springdoc.openapi.starter.webmvc.ui)
    implementation(libs.org.springdoc.springdoc.openapi.ui)
    implementation(libs.io.sentry.sentry.spring.boot.starter)
    implementation(libs.io.sentry.sentry.logback)
    implementation(libs.javax.xml.bind.jaxb.api)
    implementation(libs.org.glassfish.jaxb.jaxb.runtime)
    implementation(libs.com.fasterxml.jackson.datatype.jackson.datatype.jsr310)

    compileOnly(libs.org.projectlombok.lombok)
    annotationProcessor(libs.org.projectlombok.lombok)
    annotationProcessor("org.mapstruct:mapstruct-processor:$mapstructVersion")
    annotationProcessor("org.projectlombok:lombok-mapstruct-binding:$lombokMapstructBindingVersion")

    testImplementation(libs.org.springframework.boot.spring.boot.starter.test)
    testImplementation(libs.org.springframework.security.spring.security.test)
    testImplementation(libs.org.testcontainers.testcontainers) {
        exclude(group = "org.apache.commons", module = "commons-compress")
    }
    testImplementation(libs.org.testcontainers.postgresql)
    testImplementation(libs.io.rest.assured.rest.assured)
    testImplementation(libs.io.rest.assured.json.path)
    testImplementation(libs.com.tngtech.archunit.archunit)

    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    testCompileOnly(libs.org.projectlombok.lombok)
    testAnnotationProcessor(libs.org.projectlombok.lombok)
    testAnnotationProcessor("org.mapstruct:mapstruct-processor:$mapstructVersion")
    testAnnotationProcessor("org.projectlombok:lombok-mapstruct-binding:$lombokMapstructBindingVersion")
}

jacoco {
    toolVersion = "0.8.9"
}

tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
    jvmArgs("-Dfile.encoding=UTF-8", "-Duser.timezone=UTC")
    testLogging {
        events("failed")
        exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
    }
}

tasks.jacocoTestReport {
    dependsOn(tasks.test)
    reports {
        xml.required.set(true)
        html.required.set(true)
    }
}

tasks.test {
    finalizedBy(tasks.jacocoTestReport)
}
