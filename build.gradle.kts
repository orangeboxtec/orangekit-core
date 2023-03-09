plugins {
    kotlin("jvm") version "1.7.20"
    kotlin("plugin.allopen") version "1.7.20"
    id("io.quarkus")
    id("maven-publish")
    id("org.kordamp.gradle.jandex") version "1.1.0"
}

group = "com.orangebox.kit.core"
version = "0.0.10"

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
    implementation("io.quarkus:quarkus-resteasy-reactive-jsonb")
    implementation(enforcedPlatform("${quarkusPlatformGroupId}:${quarkusPlatformArtifactId}:${quarkusPlatformVersion}"))
    implementation(enforcedPlatform("${quarkusPlatformGroupId}:quarkus-amazon-services-bom:${quarkusPlatformVersion}"))
    implementation("io.quarkus:quarkus-resteasy-reactive")
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
    implementation("software.amazon.awssdk:netty-nio-client:2.15.32")


    testImplementation("io.quarkus:quarkus-junit5")
    testImplementation("io.rest-assured:rest-assured")
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

tasks.withType<Test> {
    systemProperty("java.util.logging.manager", "org.jboss.logmanager.LogManager")
}

allOpen {
    annotation("javax.ws.rs.Path")
    annotation("javax.enterprise.context.ApplicationScoped")
    annotation("io.quarkus.test.junit.QuarkusTest")
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions.jvmTarget = JavaVersion.VERSION_17.toString()
    kotlinOptions.javaParameters = true
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
