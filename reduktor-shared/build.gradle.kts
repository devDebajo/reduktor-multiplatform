plugins {
    kotlin("multiplatform")
    kotlin("native.cocoapods")
    id("com.android.library")
}

kotlin {
    android()
    iosX64()
    iosArm64()
    iosSimulatorArm64()

    cocoapods {
        summary = "Reduktor Shared Module"
        version = "1.0"
        ios.deploymentTarget = "14.1"
        framework {
            baseName = "reduktor-shared"
        }
    }
    
    sourceSets {
        val commonMain by getting

        val androidMain by getting

        val iosX64Main by getting
        val iosArm64Main by getting
        val iosSimulatorArm64Main by getting
        val iosMain by creating {
            dependsOn(commonMain)
            iosX64Main.dependsOn(this)
            iosArm64Main.dependsOn(this)
            iosSimulatorArm64Main.dependsOn(this)
        }
    }
}

android {
    namespace = "ru.debajo.reduktor"
    compileSdk = 32
    defaultConfig {
        minSdk = 21
        targetSdk = 32
    }
}