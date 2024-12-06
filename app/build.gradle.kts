plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("com.google.devtools.ksp")
    id("kotlin-kapt") // Untuk Glide dan penggunaan kapt
}

android {
    namespace = "com.example.perpustakaan"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.perpustakaan"
        minSdk = 24
        targetSdk = 34
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures {
        viewBinding = true
        // Matikan dataBinding jika tidak digunakan
        dataBinding = true // Hapus atau nonaktifkan jika tidak digunakan
    }
}

dependencies {
    // Room version
    val room_version = "2.6.1"

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)

    // Test dependencies
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)

    implementation(libs.androidx.activity.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.recyclerview)
    implementation(libs.material)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.serialization.json)

    // Room dependencies
    implementation("androidx.room:room-runtime:$room_version")
    implementation("androidx.room:room-ktx:$room_version")
    ksp("androidx.room:room-compiler:$room_version") // KSP support for Room

    // Optional - Test helpers
    testImplementation("androidx.room:room-testing:$room_version")

    // AndroidX Test dependencies
    androidTestImplementation(libs.androidx.espresso.core)

    // Glide dependencies (use kapt for Glide)
    implementation("com.github.bumptech.glide:glide:4.15.0")
    kapt("com.github.bumptech.glide:compiler:4.15.0")  // Gantilah ksp dengan kapt untuk Glide
}
