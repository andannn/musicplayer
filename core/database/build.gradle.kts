plugins {
    id("melodify.kmp.library")
    id("com.google.devtools.ksp")
}

kotlin {
    @Suppress("UnstableApiUsage")
    androidLibrary {
        namespace = "com.andannn.melodify.core.database"
    }

    sourceSets {
        commonMain.dependencies {
            implementation(libs.room.runtime)
            implementation(libs.sqlite.bundled)
        }
    }
}

dependencies {
    ksp(libs.room.compiler)
}