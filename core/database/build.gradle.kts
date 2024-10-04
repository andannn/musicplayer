plugins {
    id("musicplayer.android.library")
    id("musicplayer.android.room")
    id("musicplayer.android.testing")
}

android {
    namespace = "com.andannn.melodify.core.database"

    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
}
