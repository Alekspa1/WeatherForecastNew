
plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")

}

android {
    namespace = "com.drag0n.weatherf0recastn3w"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.drag0n.weatherf0recastn3w"
        minSdk = 26
        targetSdk = 33
        versionCode = 4
        versionName = "4.0"

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
    }
}

dependencies {


    implementation("com.github.bumptech.glide:glide:4.12.0")
    implementation("com.yandex.android:mobileads:6.3.0")
    implementation("com.airbnb.android:lottie:6.1.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.11.0")
    implementation("com.squareup.okhttp3:okhttp:4.11.0")
    implementation ("com.squareup.retrofit2:retrofit:2.9.0") // Retrofit
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0") // Json
    implementation("androidx.fragment:fragment-ktx:1.6.2")
    implementation("com.google.android.gms:play-services-location:21.0.1")



    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}