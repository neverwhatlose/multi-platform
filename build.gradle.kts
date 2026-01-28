plugins {
    kotlin("jvm") version "2.2.20"
    application
}

group = "org.nwtls"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))

    implementation(project(":config-lib"))
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(24)
}

application {
    mainClass.set("org.nwtls.app.MainKt")
}

sourceSets {
    main {
        kotlin.srcDir("app/src/main/kotlin")
    }
}