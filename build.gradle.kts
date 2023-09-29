buildscript {
    val kotlin_complier_exterion_version by extra("1.4.3")
    val compose_pager_version by extra("0.25.1")
    val dagger_version by extra("2.47")
    val navigation_version by extra("2.7.3")
    val lifecycle_version by extra("2.6.2")
    val hilt_navigation_compose_version by extra("1.0.0")
    val coil_version by extra("2.2.2")
    val room_version by extra("2.5.2")
    val data_store_version by extra("1.0.0")
    repositories {
        google()
    }
}// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.hilt) apply false
    id("com.android.library") version "8.1.0" apply false
    id("org.jetbrains.kotlin.android") version "1.8.10" apply false
}
