package com.andanana.melodify.util

import com.android.build.api.dsl.androidLibrary
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

fun Project.configureKotlinMultiplatform(
    extension: KotlinMultiplatformExtension,
) {
    with(extension) {
        @Suppress("UnstableApiUsage")
        androidLibrary {
            compileSdk = 34
            minSdk = 24
        }

        iosX64()
        iosArm64()
        iosSimulatorArm64()

        sourceSets.apply {
            commonMain.dependencies {
                val bom = libs.findLibrary("koin-bom").get()
                implementation(project.dependencies.platform(bom))
                implementation(libs.findLibrary("koin.core").get())
            }

            androidMain.dependencies {
                implementation(libs.findLibrary("koin.android").get())
            }

            commonTest.dependencies {
                implementation(libs.findLibrary("kotlin.test").get())
            }
        }
    }
}
