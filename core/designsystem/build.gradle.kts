plugins {
    id("musicplayer.android.library")
    id("musicplayer.android.library.compose")
}

android {
    namespace = "com.andanana.melodify.core.designsystem"

    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.material.iconsExtended)

    // Coil
    implementation(libs.coil.compose)

    implementation(libs.androidx.palette)
}
