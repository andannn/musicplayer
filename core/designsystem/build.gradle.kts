plugins {
    id("musicplayer.android.library")
    id("musicplayer.android.library.compose")
}

android {
    namespace = "com.andanana.musicplayer.core.designsystem"

    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
}

dependencies {
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.material.iconsExtended)

    // Coil
    implementation(libs.coil.compose)
}
