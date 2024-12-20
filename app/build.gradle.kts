
plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id ("kotlin-kapt")
    id ("com.huawei.agconnect")
    id ("dagger.hilt.android.plugin")
}

android {
    namespace = "com.drag0n.weatherf0recastn3w"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.drag0n.weatherf0recastn3w"
        minSdk = 26
        targetSdk = 35

        versionCode = 23
        versionName = "9.1"



        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            kapt {
                arguments {arg("room.schemaLocation", "$projectDir/schemas")}
            }
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation ("com.google.dagger:hilt-android:2.51.1")
    kapt ("com.google.dagger:hilt-compiler:2.51.1")

    

    implementation( "ru.rustore.sdk:billingclient:5.1.1" )

    implementation ("com.huawei.hms:location:5.0.0.301")
    implementation ("com.huawei.agconnect:agconnect-core:1.9.1.301")



    implementation ("androidx.room:room-runtime:2.6.1")
    kapt ("androidx.room:room-compiler:2.6.1")
    implementation ("androidx.room:room-ktx:2.6.1")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.8.7")
    implementation("androidx.fragment:fragment-ktx:1.8.5")


    implementation ("com.squareup.picasso:picasso:2.8")
    implementation("com.yandex.android:mobileads:7.8.0")
    implementation("com.yandex.ads.mediation:mobileads-mytarget:5.22.1.4")

    implementation("com.airbnb.android:lottie:6.1.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.11.0")
    implementation("com.squareup.okhttp3:okhttp:4.11.0")
    implementation ("com.squareup.retrofit2:retrofit:2.9.0") // Retrofit
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0") // Json
    implementation("androidx.fragment:fragment-ktx:1.8.5")
    implementation("com.google.android.gms:play-services-location:21.3.0")



    implementation("androidx.core:core-ktx:1.15.0")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.2.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
}