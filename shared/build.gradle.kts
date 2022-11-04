plugins {
    kotlin("multiplatform")
    kotlin("native.cocoapods")
    kotlin("plugin.serialization")
    id("com.android.library")
    id("org.jetbrains.compose") version "1.2.0"
}

compose {
    android {
        useAndroidX = true
    }
}

kotlin {
    android()
    iosX64()
    iosArm64()
    iosSimulatorArm64()

    cocoapods {
        summary = "Some description for the Shared Module"
        homepage = "Link to the Shared Module homepage"
        version = "1.0"
        ios.deploymentTarget = "14.1"
        podfile = project.file("../iosApp/Podfile")
        framework {
            baseName = "shared"
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                api("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.3")
                api("io.ktor:ktor-client-core:2.0.2")
                api("io.ktor:ktor-client-logging:2.0.2")

                implementation(compose.ui)
                implementation(compose.foundation)
                implementation(compose.material)
                implementation(compose.runtime)

                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.2")
            }

        }

        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        val androidMain by getting {
            dependencies {
                implementation("com.google.accompanist:accompanist-permissions:0.26.0-alpha")
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.4")
                implementation("io.ktor:ktor-client-okhttp:2.0.2")
            }
        }
        val iosX64Main by getting
        val iosArm64Main by getting
        val iosSimulatorArm64Main by getting
        val iosMain by creating {
            dependsOn(commonMain)
            iosX64Main.dependsOn(this)
            iosArm64Main.dependsOn(this)
            iosSimulatorArm64Main.dependsOn(this)

            dependencies {
                implementation("io.ktor:ktor-client-ios:2.0.2")
            }
        }
    }
}

android {
    namespace = "ru.debajo.reduktor.demo"
    compileSdk = 32
    defaultConfig {
        minSdk = 21
        targetSdk = 32
    }
}
dependencies {
    implementation("androidx.core:core:1.9.0")
    implementation("com.google.android.gms:play-services-location:21.0.0")
}
