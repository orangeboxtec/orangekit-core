import org.kordamp.gradle.plugin.jandex.tasks.JandexTask

plugins {
    kotlin("jvm") version "2.0.21"
    kotlin("plugin.allopen") version "2.0.21"
    id("io.quarkus")
    id("maven-publish")
    id("org.kordamp.gradle.jandex") version "2.1.0"
}

group = "com.orangebox.kit.core"
version = "2.1.0"

repositories {
    mavenCentral()
    mavenLocal()
    maven {
        url = uri("https://artifactory.startup-kit.net/artifactory/orangekit")
    }
}

val quarkusPlatformGroupId: String by project
val quarkusPlatformArtifactId: String by project
val quarkusPlatformVersion: String by project

dependencies {
    implementation("io.quarkus:quarkus-rest-jsonb")
    implementation(platform("${quarkusPlatformGroupId}:${quarkusPlatformArtifactId}:${quarkusPlatformVersion}"))
    implementation(platform("${quarkusPlatformGroupId}:quarkus-amazon-services-bom:${quarkusPlatformVersion}"))
    implementation("io.quarkus:quarkus-kotlin")
    implementation("io.quarkiverse.amazonservices:quarkus-amazon-s3")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("io.quarkus:quarkus-arc")
    implementation("io.quarkus:quarkus-mongodb-client")
    implementation("org.apache.commons:commons-lang3")
    implementation("com.jayway.jsonpath:json-path:2.7.0")
    implementation("commons-io:commons-io")
    implementation("net.coobird:thumbnailator:0.4.19")
    implementation("commons-codec:commons-codec")
    implementation("software.amazon.awssdk:apache-client")
    implementation("software.amazon.awssdk:netty-nio-client")
    implementation("software.amazon.awssdk:url-connection-client")


    testImplementation("io.quarkus:quarkus-junit5")
    testImplementation("io.rest-assured:rest-assured")
}

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

tasks.withType<Test> {
    systemProperty("java.util.logging.manager", "org.jboss.logmanager.LogManager")
}

tasks.withType<JandexTask> {
    dependsOn(":quarkusDependenciesBuild")
}

allOpen {
    annotation("javax.ws.rs.Path")
    annotation("javax.enterprise.context.ApplicationScoped")
    annotation("io.quarkus.test.junit.QuarkusTest")
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
        }
    }
    repositories {
        maven {
            name = "orangebox"
            url = uri("https://artifactory.startup-kit.net/artifactory/orangekit")
            credentials(PasswordCredentials::class)
        }
    }
}
