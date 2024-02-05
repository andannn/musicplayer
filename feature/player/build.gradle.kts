plugins {
    id("musicplayer.android.library")
    id("musicplayer.android.hilt")
    id("musicplayer.android.library.compose")
}

android {
    namespace = "com.andanana.musicplayer.feature.player"

    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
}

dependencies {
    implementation(project(":core:designsystem"))
    implementation(project(":core:data"))
    implementation(project(":core:player"))

// TODO: remove this dependency.
    implementation(project(":core:datastore"))
    implementation(project(":core:mediastore"))

    implementation(libs.kotlinx.coroutines.guava)
    implementation(libs.androidx.core.ktx)

    api(libs.androidx.compose.material3)
    api(libs.androidx.compose.material)
    api(libs.androidx.compose.material.iconsExtended)

    // Navigation
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.androidx.navigation.compose)

    // Coil
    implementation(libs.coil.compose)

    // Media3
    implementation(libs.androidx.media3.session)

    implementation("com.github.skydoves:flexible-bottomsheet-material3:0.1.2")
}
