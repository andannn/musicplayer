import com.android.build.api.dsl.androidLibrary

plugins {
    id("melodify.kmp.library")
}

kotlin {
    @Suppress("UnstableApiUsage")
    androidLibrary {
        namespace = "com.andannn.melodify.core.datastore"
    }

    sourceSets {
        commonMain.dependencies {
            implementation(libs.datastore)
            implementation(libs.datastore.preferences)
        }
    }
}
