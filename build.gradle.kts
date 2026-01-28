plugins {
    kotlin("jvm") version "2.2.20"
}

group = "org.neverwhatlose"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))

    // Source: https://mvnrepository.com/artifact/org.yaml/snakeyaml
    implementation("org.yaml:snakeyaml:2.5")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(24)
}