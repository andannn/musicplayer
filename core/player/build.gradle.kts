plugins {
    id("musicplayer.android.library")
    id("musicplayer.android.hilt")
}

android {
    namespace = "com.andanana.musicplayer.feature.core.player"

    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
}

dependencies {
    implementation(project(":core:model"))

    // Media3
    implementation(libs.media3.exoplayer)
    implementation(libs.androidx.media)
}
