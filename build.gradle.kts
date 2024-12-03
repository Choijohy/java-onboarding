plugins {
    java
    id("org.springframework.boot") version "3.3.0"
    id("io.spring.dependency-management") version "1.1.5"
    id("org.asciidoctor.jvm.convert") version "4.0.2"
}

group = "com.example"
version = "0.0.1-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_17
}

repositories {
    mavenCentral()
}

val asciidoctorExt = "asciidoctorExt"
configurations.create(asciidoctorExt) {
    extendsFrom(configurations.testImplementation.get())
}

dependencies {
    // spring
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    asciidoctorExt("org.springframework.restdocs:spring-restdocs-asciidoctor")


    // database
    implementation("org.mariadb.jdbc:mariadb-java-client")

    // lombok
    implementation("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")

    // flyway
    implementation("org.flywaydb:flyway-core")
    implementation("org.flywaydb:flyway-mysql")

    // querydsl
    implementation ("com.querydsl:querydsl-jpa:5.0.0:jakarta")
    annotationProcessor("com.querydsl:querydsl-apt:5.0.0:jakarta")
    annotationProcessor("jakarta.annotation:jakarta.annotation-api")
    annotationProcessor("jakarta.persistence:jakarta.persistence-api")

    // swagger
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.0.2")

    // test
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.restdocs:spring-restdocs-mockmvc")
    testImplementation("com.h2database:h2")

    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    // TestContainer
    testImplementation("org.springframework.boot:spring-boot-testcontainers")
    testImplementation("org.testcontainers:junit-jupiter")
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.testcontainers:mariadb")
    testImplementation("org.testcontainers:jdbc")
}

tasks.withType<Test> {
    useJUnitPlatform()
}


