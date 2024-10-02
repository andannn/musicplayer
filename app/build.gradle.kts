plugins {
    id("musicplayer.android.application")
    id("musicplayer.android.application.compose")
    id("musicplayer.android.hilt")
    id("musicplayer.android.testing")
}

android {
    namespace = "com.andannn.melodify"

    defaultConfig {
        applicationId = "com.andannn.melodify"
        versionCode = 11
        versionName = "0.1.5"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
        signingConfig = signingConfigs.getByName("debug")
    }

    signingConfigs {
        create("release") {
            storeFile = file("keystore/keystore.jks")
            storePassword = System.getenv("SIGNING_STORE_PASSWORD")
            keyAlias = System.getenv("SIGNING_KEY_ALIAS")
            keyPassword = System.getenv("SIGNING_KEY_PASSWORD")
        }
    }

    lint {
        baseline = file("lint-baseline.xml")
    }

    buildTypes {
        debug {
            applicationIdSuffix = ".debug"
            versionNameSuffix = "-debug"
        }

        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )

            signingConfig = signingConfigs.getByName("release")
        }
    }
}

tasks.register("moveKeyStoreRelease") {
    if (project.gradle.startParameter.taskNames.any { it.contains("Release") }) {
        val tmpFilePath = System.getProperty("user.home") + "/work/_temp/keystore/"
        val allFilesFromDir = File(tmpFilePath).listFiles()
        if (allFilesFromDir != null) {
            val keystoreFile = allFilesFromDir.firstOrNull()

            if (keystoreFile == null || keystoreFile.name != "keystore.jks") {
                throw GradleException("File not found: $tmpFilePath Aborting build.")
            }

            copy {
                from(keystoreFile.absolutePath)
                into("$projectDir/keystore")
            }
        } else {
            throw GradleException("File not found: $tmpFilePath Aborting build.")
        }
    } else {
        println("Debug mode. skip moving keystore file.")
    }
}

tasks.named("preBuild") {
    dependsOn("moveKeyStoreRelease")
}

dependencies {
    implementation(project(":core:designsystem"))
    implementation(project(":core:domain"))
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

    implementation(libs.androidx.compose.material3)

    // Firebase
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.crashlytics)
}
