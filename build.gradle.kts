plugins {
    kotlin("jvm").version("1.4.0")
}

group "org.pampanet.edu"
version "1.0.0"

repositories {
    mavenCentral()
}

kotlin{
    /*test {
        useJUnitPlatform()
        systemProperties = mapOf(
                Pair("junit.jupiter.extensions.autodetection.enabled", "true"),
                Pair("junit.jupiter.testinstance.lifecycle.default", "per_class")
        )
    }*/
}

dependencies {
    implementation(kotlin("stdlib"))
    testImplementation(kotlin("test-junit5"))

    /*testImplementation("org.mockito:mockito-core:2.21.0")
    testImplementation("org.mockito:mockito-junit-jupiter:2.23.0")
    //testImplementation("com.google.truth:truth:1.0.1")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.6.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.6.2")*/
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.6.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.6.2")
}

tasks.test {
    useJUnitPlatform()
    systemProperties = mapOf(
            Pair("junit.jupiter.extensions.autodetection.enabled", "true"),
            Pair("junit.jupiter.testinstance.lifecycle.default", "per_class")
    )
}
