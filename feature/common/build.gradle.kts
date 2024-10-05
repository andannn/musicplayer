plugins {
    id("melodify.kmp.library")
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.compose.compiler)
}

kotlin {
    sourceSets {
        androidMain.dependencies {
            implementation(compose.preview)
            implementation(libs.androidx.activity.compose)
            implementation(libs.material.color.utilities.android)
            implementation(libs.androidx.palette)
        }

        commonMain.dependencies {
            implementation(project(":core:data"))
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.materialIconsExtended)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodel)
            implementation(libs.androidx.lifecycle.runtime.compose)
            implementation(libs.coil3.compose)
        }
    }
}

compose.resources {
    publicResClass = true
    generateResClass = auto
}

android {
    namespace = "com.andannn.melodify.common"
}
