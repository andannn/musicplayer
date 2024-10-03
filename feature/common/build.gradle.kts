plugins {
    id("musicplayer.android.library")
    id("musicplayer.android.hilt")
    id("musicplayer.android.library.compose")
}

android {
    namespace = "com.andannn.melodify.common"

    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
    }
}

dependencies {
    implementation(project(":core:domain"))
    implementation(project(":core:data"))

    // Coil
    implementation(libs.coil.compose)

    implementation(libs.androidx.compose.material3)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.material.iconsExtended)

    implementation(libs.androidx.palette)

    implementation(libs.material.color.utilities.android)
}
