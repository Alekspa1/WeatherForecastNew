// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript {
    repositories {
        maven {url = uri("https://developer.huawei.com/repo/") }
    }
    dependencies {
        classpath("com.android.tools.build:gradle:8.1.0")
        classpath("com.huawei.agconnect:agcp:1.9.0.300")
    }
}


plugins {
    id ("com.android.library") version "7.3.1" apply false
    id("com.android.application") version "8.1.1" apply false
    id("org.jetbrains.kotlin.android") version "1.8.0" apply false
}
