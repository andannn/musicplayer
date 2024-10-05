package com.andanana.melodify.util

import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

@ExperimentalKotlinGradlePluginApi
fun Project.configureKotlinMultiplatform(
    extension: KotlinMultiplatformExtension,
) {
    with(extension) {
        compilerOptions {
            androidTarget {
                compilerOptions.jvmTarget.set(JvmTarget.JVM_17)
            }
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
