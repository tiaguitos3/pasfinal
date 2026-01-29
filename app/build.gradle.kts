plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.pas"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.pas"
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
    // Core Android
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.constraintlayout)
    implementation(libs.activity)

    // Lifecycle (ViewModel e LiveData)
    implementation(libs.lifecycle.viewmodel)
    implementation(libs.lifecycle.livedata)

    // Retrofit (HTTP client para API)
    implementation(libs.retrofit)
    implementation(libs.converter.gson)

    // Glide (Carregamento de imagens)
    implementation(libs.glide)

    // Room (Base de dados local)
    implementation(libs.room.runtime)
    annotationProcessor(libs.room.compiler)

    // Testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}