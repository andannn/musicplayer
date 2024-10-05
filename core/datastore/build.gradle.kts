plugins {
    id("melodify.kmp.library")
}

android {
    namespace = "com.andannn.melodify.core.datastore"
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.datastore)
            implementation(libs.datastore.preferences)
        }
    }
}
