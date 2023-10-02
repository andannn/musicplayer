plugins {
    id("musicplayer.android.library")
    id("musicplayer.android.room")
    id("musicplayer.android.hilt")
    id("musicplayer.android.testing")
}

android {
    namespace = "com.andanana.musicplayer.core.database"

    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
}

dependencies {
    implementation(project(":core:mediastore"))
}