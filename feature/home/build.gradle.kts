plugins {
    id("musicplayer.android.library")
    id("musicplayer.android.library.compose")
}

android {
    namespace = "com.andannn.melodify.feature.home"

    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
}

dependencies {
    implementation(project(":core:data"))
    implementation(project(":feature:common"))

    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.material.iconsExtended)

    // Navigation
    implementation(libs.androidx.navigation.compose)
}
