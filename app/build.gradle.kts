plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.ksp)
    alias(libs.plugins.compose.compiler) // Handles compose compiler configuration dynamically
}

android {
    namespace = "com.example.secure_it_all"
    compileSdk = 37

    defaultConfig {
        applicationId = "com.example.secure_it_all"
        minSdk = 24
        targetSdk = 37
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures {
        compose = true
    }

    buildTypes {
        release {
            optimization {
                enable = false
            }
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

// REMOVED: The configurations.all { resolutionStrategy { ... } } block has been removed.
// AGP 9.4.0 cleanly aligns modern dependency constraints automatically for compileSdk 37.

dependencies {
    // Jetpack Compose Foundation Toolkit
    implementation("androidx.compose.ui:ui:1.7.8")
    implementation("androidx.compose.ui:ui-tooling-preview:1.7.8")
    implementation("androidx.compose.material3:material3:1.3.1")

    // Core Lifecycle & Activity bindings
    implementation("androidx.activity:activity-compose:1.10.1") // Reverted safely to your preferred version
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.4")

    // Room (FIX: Upgraded versions to fix KSP2 signature bug)
    implementation("androidx.room:room-runtime:2.7.0")
    implementation("androidx.room:room-ktx:2.7.0")
    ksp("androidx.room:room-compiler:2.7.0")

    // Coroutines / Flow
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.1")

    // Background WorkManager Tasks
    implementation("androidx.work:work-runtime-ktx:2.9.1")

    // Navigation Structure
    implementation("androidx.navigation:navigation-compose:2.7.7")
    implementation("androidx.compose.material:material-icons-extended")

    implementation(libs.material)
}
