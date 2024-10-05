plugins {
    id("musicplayer.android.library")
    id("musicplayer.android.room")
    id("musicplayer.android.testing")
}

android {
    namespace = "com.andannn.melodify.core.data"

    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
}

dependencies {
    implementation(project(":core:datastore"))
    implementation(project(":core:player"))
    implementation(project(":core:network"))
    implementation(project(":core:database"))

    implementation(libs.kotlinx.coroutines.guava)

    implementation(libs.androidx.media3.common)
    implementation(libs.androidx.media3.session)
}
