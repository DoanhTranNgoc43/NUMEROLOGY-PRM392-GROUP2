plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.numerology_prm392_group2"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.numerology_prm392_group2"
        minSdk = 24
        targetSdk = 35
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
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.swiperefreshlayout)
    implementation(libs.ui.desktop)
    compileOnly(libs.lombok)
    annotationProcessor(libs.lombok)

    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.okhttp)
    implementation(libs.logging.interceptor)
    
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation (libs.retrofit)
    implementation (libs.converter.gson)


    // Gson for JSON serialization
    implementation (libs.gson)

    // OkHttp (used by Retrofit)
    implementation (libs.okhttp)
    implementation (libs.logging.interceptor)

    // Material Components (if not already included)
    implementation (libs.material.v1110)
    implementation (libs.glide)
    annotationProcessor (libs.compiler)

    implementation(libs.room.runtime)
    annotationProcessor(libs.room.compiler)
// Nếu dùng Kotlin: kapt "androidx.room:room-compiler:2.6.1"


}