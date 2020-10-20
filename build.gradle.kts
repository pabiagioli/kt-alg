plugins {
    kotlin("multiplatform").version("1.4.0")
    //kotlin("jvm").version("1.4.0")
}

group "org.pampanet.edu"
version "1.0.0"

repositories {
    mavenCentral()
}

kotlin{

    // Determine host preset.
    val hostOs = System.getProperty("os.name")

    // Create target for the host platform.
    val hostTarget = when {
        hostOs == "Mac OS X" -> macosX64("native")
        hostOs == "Linux" -> linuxX64("native")
        hostOs.startsWith("Windows") -> mingwX64("native")
        else -> throw GradleException("Host OS '$hostOs' is not supported in Kotlin/Native $project.")
    }

    jvm {
        withJava()
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(kotlin("stdlib"))
                implementation(kotlin("stdlib-common"))
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }

        val nativeMain by getting {
            dependsOn(commonMain)
        }
        val nativeTest by getting {
            dependsOn(commonTest)
        }

        val jvmMain by getting {
            dependsOn(commonMain)
        }
        val jvmTest by getting {
            dependsOn(commonTest)
            //useJUnitPlatform()
            dependencies {
                implementation(kotlin("test-junit5"))
                //implementation("org.mockito:mockito-junit-jupiter:2.23.0")
                implementation("org.junit.jupiter:junit-jupiter-params:5.6.2")
                runtimeOnly("org.junit.jupiter:junit-jupiter-engine:5.6.2")
            }
        }
    }

    sourceSets.all {
        languageSettings.useExperimentalAnnotation("kotlin.ExperimentalStdlibApi")
    }
}
project.tasks.withType(Test::class.java).all {
    jvmArgs(listOf("-Xmx2g","-Xss1g"))
    useJUnitPlatform()
    systemProperties = mapOf(
        Pair("junit.jupiter.extensions.autodetection.enabled", "true"),
        Pair("junit.jupiter.testinstance.lifecycle.default", "per_class")
    )
}

