plugins {
    id("melodify.kmp.library")
}

android {
    namespace = "com.andannn.melodify.core.data"
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(project(":core:datastore"))
            implementation(project(":core:network"))
            implementation(project(":core:database"))
            implementation(project(":core:player"))
        }
    }
}

dependencies {
    implementation(libs.kotlinx.coroutines.guava)

    implementation(libs.androidx.media3.common)
    implementation(libs.androidx.media3.session)
}
