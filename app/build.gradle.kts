plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    kotlin("plugin.serialization") version "2.0.21"
    id("androidx.navigation.safeargs.kotlin")
    id("org.jetbrains.kotlin.plugin.compose")
    //kapt
    id("kotlin-kapt")
}

android {
    namespace = "com.ficusflower.pomodoroasmr"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.ficusflower.pomodoroasmr"
        minSdk = 27
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }

    buildFeatures {
        viewBinding = true
        compose = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.foundation.layout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    val navVersion = "2.9.3"

    // Jetpack Compose integration
    implementation("androidx.navigation:navigation-compose:$navVersion")

    // Views/Fragments integration
    implementation("androidx.navigation:navigation-fragment:$navVersion")
    implementation("androidx.navigation:navigation-ui:$navVersion")

    // Feature module support for Fragments
    implementation("androidx.navigation:navigation-dynamic-features-fragment:$navVersion")

    // Testing Navigation
    androidTestImplementation("androidx.navigation:navigation-testing:$navVersion")

    // JSON serialization library, works with the Kotlin serialization plugin
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.3")

    //livedata
    implementation("androidx.lifecycle:lifecycle-livedata:2.9.4")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.10.0")

    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.10.0")

    testImplementation("androidx.arch.core:core-testing:2.2.0")

    // Импортируем BOM
    val composeBom = platform("androidx.compose:compose-bom:2024.02.01")
    implementation(composeBom)
    androidTestImplementation(composeBom)

    // Добавляем основные библиотеки (версии указывать не нужно, их подтянет BOM)
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.ui:ui-tooling-preview")
    debugImplementation("androidx.compose.ui:ui-tooling")

    // Интеграция с Activity и ViewModel
    implementation("androidx.activity:activity-compose:1.8.2")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0")

    implementation("androidx.navigation:navigation-compose:2.8.9")

    val coroutines_version = "1.11.0"
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutines_version")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutines_version")

    //Room
    val room_version = "2.8.4"

    implementation("androidx.room:room-runtime:$room_version")
    implementation("androidx.room:room-ktx:$room_version")

    //kapt
    kapt("androidx.room:room-compiler:$room_version")

    // Сама библиотека Koin для Android
    implementation("io.insert-koin:koin-android:4.2.1")
    // Поддержка Jetpack Compose
    implementation("io.insert-koin:koin-androidx-compose:4.2.1")

    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:$coroutines_version")

    androidTestImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:$coroutines_version")

    testImplementation("io.mockk:mockk:1.14.11")
}

buildscript {
    dependencies {
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:2.9.3")
    }
}