import org.jetbrains.kotlin.cfg.pseudocode.and

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
        }

        androidMain.dependencies {
            implementation(project(":core:player"))
        }
    }
}

dependencies {
    implementation(libs.kotlinx.coroutines.guava)

    implementation(libs.androidx.media3.common)
    implementation(libs.androidx.media3.session)
}
