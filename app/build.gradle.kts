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
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        val release by getting {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
}

dependencies {
    implementation(project(":core:designsystem"))
    implementation(project(":core:mediastore"))
    implementation(project(":core:player"))
    implementation(project(":core:model"))
    implementation(project(":core:database"))
    implementation(project(":core:datastore"))

    implementation(project(":feature:home"))
    implementation(project(":feature:library"))
    implementation(project(":feature:player"))
    implementation(project(":feature:playlist"))
    implementation(project(":feature:playqueue"))

    implementation(libs.androidx.core.ktx)

    implementation(libs.androidx.core.splashscreen)

    implementation(libs.accompanist.systemuicontroller)

    // Activity
    implementation(libs.androidx.activity.compose)

    // Navigation
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.androidx.navigation.compose)

    api(libs.androidx.compose.material3)
    api(libs.androidx.compose.material)
}