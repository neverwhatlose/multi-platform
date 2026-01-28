plugins {
    kotlin("jvm")
}

group = "org.nwtls"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    implementation(libs.snake.yaml)
}

kotlin {
    jvmToolchain(24)
}

tasks.test {
    useJUnitPlatform()
}