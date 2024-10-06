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
    }
}

dependencies {
    ksp(libs.room.compiler)
}