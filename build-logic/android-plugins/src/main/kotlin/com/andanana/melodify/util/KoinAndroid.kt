package com.andanana.melodify.util

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

/**
 * Configure base Kotlin with Android options
 */
internal fun Project.configureKoinAndroid(commonExtension: CommonExtension<*, *, *, *, *>) {
    dependencies {
        val bom = libs.findLibrary("koin-bom").get()
        add("implementation", platform(bom))
        "implementation"(libs.findLibrary("koin.core").get())
        "implementation"(libs.findLibrary("koin.android").get())
    }
}