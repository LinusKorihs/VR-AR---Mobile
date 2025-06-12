plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.ksp)
    id("kotlin-parcelize")
    id("org.jetbrains.kotlin.plugin.serialization") version "2.0.0"
}

android {
    namespace = "com.example.AbgabeMobile"
    compileSdk = 35 // The API level to compile against

    defaultConfig {
        applicationId = "com.example.AbgabeMobile" // Unique identifier for the application
        minSdk = 35 // Minimum API level required to run the app
        targetSdk = 35 // API level the app is targeting
        versionCode = 1 // Internal version number
        versionName = "1.0" // User-visible version name

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner" // Test runner for Android instrumentation tests
    }

    buildTypes {
        release {
            isMinifyEnabled = false // Whether to enable code shrinking for the release build
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"), // Default ProGuard rules
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11 // Java version for source code
        targetCompatibility = JavaVersion.VERSION_11 // Java version for compiled bytecode
    }
    kotlinOptions {
        jvmTarget = "11" // JVM version for Kotlin compilation
    }
    buildFeatures {
        compose = true // Enable Jetpack Compose
    }
}

dependencies {
    // Core Android libraries
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    // Jetpack Compose
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.ui.tooling.preview)
    debugImplementation(libs.androidx.ui.tooling) // For Compose tooling in debug builds
    debugImplementation(libs.androidx.ui.test.manifest) // For Compose UI tests
    // Testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom)) // For Compose UI tests
    androidTestImplementation(libs.androidx.ui.test.junit4) // For Compose UI tests

    // Ktor for networking
    implementation(libs.ktor.client.cio)
    implementation(libs.ktor.client.core)
    implementation(libs.kotlinx.serialization.json)
    implementation("io.ktor:ktor-client-content-negotiation:3.1.3")
    implementation("io.ktor:ktor-serialization-kotlinx-json:3.1.3")
    implementation(libs.qrcode.kotlin)
    implementation(libs.core)
    implementation(libs.androidx.navigation.compose) //v277
    implementation(libs.gson)
    // CameraX for camera functionalities
    implementation(libs.androidx.camera.camera2)
    implementation(libs.androidx.camera.lifecycle)
    implementation(libs.androidx.camera.view)
    // ML Kit Barcode Scanning
    implementation(libs.barcode.scanning)
    // Accompanist for permissions handling
    implementation(libs.accompanist.permissions)
    // Material 3 Adaptive Navigation Suite
    implementation(libs.androidx.material3.adaptive.navigation.suite)
    // Room for local database
    implementation ("androidx.room:room-runtime:2.7.1")
    ksp ("androidx.room:room-compiler:2.7.1")
    implementation (libs.room.ktx)
    implementation (libs.androidx.room.paging)
    // Coil for image loading
    implementation(libs.coil.compose)
}