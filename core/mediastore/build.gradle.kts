plugins {
    id("musicplayer.android.library")
    id("musicplayer.android.hilt")
}

android {
    namespace = "com.andanana.melodify.core.mediastore"

    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
}
