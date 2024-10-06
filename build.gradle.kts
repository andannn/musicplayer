buildscript {
    repositories {
        google()
        mavenCentral()
    }
}// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.kotlinMultiplatform).apply(false)
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.serialization) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.compose.compiler) apply false
    alias(libs.plugins.jetbrainsCompose) apply false
    alias(libs.plugins.google.service) apply false
    alias(libs.plugins.firebase.crashlytics) apply false
}
