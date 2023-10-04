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
    api(libs.androidx.compose.material3)
    api(libs.androidx.compose.material)
    api(libs.androidx.compose.material.iconsExtended)

    // Navigation
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.androidx.navigation.compose)

    // Coil
    implementation(libs.coil.compose)

    // Compose pager
    implementation("com.google.accompanist:accompanist-pager:0.25.1")
}
