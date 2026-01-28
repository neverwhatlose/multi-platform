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

    implementation(libs.snake.yaml)
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(24)
}