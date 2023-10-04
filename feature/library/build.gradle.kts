plugins {
    id("musicplayer.android.library")
    id("musicplayer.android.hilt")
    id("musicplayer.android.library.compose")
}

android {
    namespace = "com.andanana.musicplayer.feature.library"

    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
}

dependencies {
    implementation(project(":core:database"))
    implementation(project(":core:designsystem"))
    implementation(project(":core:data"))

    implementation(libs.androidx.core.ktx)

    api(libs.androidx.compose.material3)
    api(libs.androidx.compose.material)
    api(libs.androidx.compose.material.iconsExtended)

    // Navigation
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.androidx.navigation.compose)

    // Coil
    implementation(libs.coil.compose)
}
