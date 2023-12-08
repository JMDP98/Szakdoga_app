buildscript {
    repositories {
        // ...
        google()
        mavenCentral()
        jcenter()
        // ...
    }
    dependencies {
        classpath("com.google.gms:google-services:4.4.0")
    }
}
// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.1.0" apply false
    id("org.jetbrains.kotlin.android") version "1.8.0" apply false
    kotlin("jvm") version "1.9.10"

    //id("com.google.gms.google-services") version "4.3.15" apply false
}