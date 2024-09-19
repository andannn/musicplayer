plugins {
    id("musicplayer.android.library")
    id("musicplayer.android.hilt")
    id("musicplayer.android.room")
    id("musicplayer.android.testing")
}

android {
    namespace = "com.andanana.musicplayer.core.data"

    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
}

dependencies {
    implementation(project(":core:mediastore"))
    implementation(project(":core:datastore"))
    implementation(project(":core:model"))
    implementation(project(":core:player"))

    implementation(libs.kotlinx.coroutines.guava)

    implementation(libs.androidx.media3.common)
    implementation(libs.androidx.media3.exoplayer)
    implementation(libs.androidx.media3.session)
}
