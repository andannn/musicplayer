plugins {
    id("melodify.kmp.library")
    id("com.google.devtools.ksp")
}

android {
    namespace = "com.andannn.melodify.feature.database"
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.room.runtime)
        }

        iosMain.dependencies {
            implementation(libs.androidx.sqlite.bundled)
        }
    }
}

dependencies {
    ksp(libs.room.compiler)
}