plugins {
    id("musicplayer.android.library")
    id("musicplayer.android.hilt")
}

android {
    namespace = "com.andanana.musicplayer.core.model"

    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
}

dependencies {
    implementation(libs.androidx.media3.common)
}
