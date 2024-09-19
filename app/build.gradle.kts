plugins {
    id("musicplayer.android.application")
    id("musicplayer.android.application.compose")
    id("musicplayer.android.hilt")
    id("musicplayer.android.testing")
}

android {
    namespace = "com.andanana.musicplayer"

    defaultConfig {
        applicationId = "com.andanana.musicplayer"
        versionCode = 2
        versionName = "0.0.1-alpha7"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
        signingConfig = signingConfigs.getByName("debug")
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
    }
}

dependencies {
    implementation(project(":core:designsystem"))
    implementation(project(":core:data"))
    implementation(project(":core:model"))
    implementation(project(":core:player"))

    implementation(project(":feature:common"))
    implementation(project(":feature:home"))
    implementation(project(":feature:player"))
    implementation(project(":feature:playlist"))

    implementation(libs.androidx.core.ktx)

    implementation(libs.androidx.core.splashscreen)

    // Activity
    implementation(libs.androidx.activity.compose)

    // Navigation
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.androidx.navigation.compose)

    api(libs.androidx.compose.material3)
    api(libs.androidx.compose.material)

    // Media3
    implementation(libs.androidx.media3.exoplayer)
    implementation(libs.androidx.media3.session)

    // Firebase
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.crashlytics)
}
