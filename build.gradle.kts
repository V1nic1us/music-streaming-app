// Top-level build file where you can add configuration options common to all sub-projects/modules.
//buildscript {
//    repositories {
//        // Make sure you have the following two repositories
//        google() // Google's Maven repository
//
//        mavenCentral() // Maven Central repository
//    }
//
//    dependencies {
//        // Add the dependency for the Google services Gradle plugin
//        classpath("com.google.gms:google-services:4.3.15")
//    }
//}
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
    id("com.google.gms.google-services") version "4.4.2" apply false
}