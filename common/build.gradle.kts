import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

plugins {
    kotlin("multiplatform")
    kotlin("native.cocoapods")
    id("com.android.library")
    kotlin("plugin.serialization") version "1.5.30"
}

version = "0.1.0"

kotlin {
    android()

    val iosTarget: (String, KotlinNativeTarget.() -> Unit) -> KotlinNativeTarget =
        if (System.getenv("SDK_NAME")?.startsWith("iphoneos") == true)
            ::iosArm64
        else
            ::iosX64

    iosTarget("ios") {}

    cocoapods {
        summary = "Some description for the Common Module"
        homepage = "Link to the Common Module homepage"
        ios.deploymentTarget = "14.1"
        frameworkName = "common"
        podfile = project.file("../iosApp/Podfile")
    }
    
    sourceSets {
        val coroutinesVersion = "1.5.1"
        val kableVersion = "0.7.1"
        val ktorIoVersion = "1.6.3"

        val commonMain by getting {
            dependencies {
                api("org.jetbrains.kotlinx:kotlinx-coroutines-core:${coroutinesVersion}")
                api("com.juul.kable:core:${kableVersion}")
                implementation("io.ktor:ktor-io:${ktorIoVersion}")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }

        val androidMain by getting {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:${coroutinesVersion}")
            }
        }
        val androidTest by getting {
            dependencies {
                implementation(kotlin("test-junit"))
                implementation("junit:junit:4.13.2")
            }
        }

        val iosMain by getting {
            dependencies {
                implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-core:${coroutinesVersion}-native-mt"){
                    version {
                        strictly("${coroutinesVersion}-native-mt")
                    }
                }
            }
        }
        val iosTest by getting
    }
}

android {
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    compileSdk = 31
    defaultConfig {
        minSdk = 21
        targetSdk = 31
    }
}
