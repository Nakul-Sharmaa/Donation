plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.donation"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.donation"
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
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation ("androidx.drawerlayout:drawerlayout:1.2.0")

    // AndroidX AppCompat dependency
    implementation ("androidx.appcompat:appcompat:1.7.0")

    // Google Material Components dependency
    implementation ("com.google.android.material:material:1.12.0")
}