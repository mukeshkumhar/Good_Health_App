plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("com.chaquo.python")
}

android {
    namespace = "com.example.goodhealth"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.goodhealth"
        minSdk = 27
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        ndk {
            // On Apple silicon, you can omit x86_64.
            abiFilters += listOf("arm64-v8a", "x86_64")
        }


        chaquopy {
            defaultConfig {
                version = "3.9"
            }

        }
        chaquopy {
            sourceSets {
                getByName("main") {
                    srcDir("usr/bin/python3")
                }
            }
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    ndkVersion = "25.1.8937393"
    buildToolsVersion = "34.0.0"
//    sourceSets.getByName("main") {
//        setSrcDirs(listOf("some/other/main/python"))
//    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    implementation("com.google.ai.client.generativeai:generativeai:0.9.0")
    implementation("androidx.compose.ui:ui:1.6.8") // Or latest version
    implementation("androidx.compose.material:material:1.6.8") // Or latest version
    implementation("androidx.compose.ui:ui-tooling-preview:1.6.8") // For previews
    implementation("androidx.appcompat:appcompat:1.7.0")

    implementation ("org.tensorflow:tensorflow-lite-task-vision-play-services:0.4.4")
    implementation ("com.google.android.gms:play-services-tflite-gpu:16.2.0")

}