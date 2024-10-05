plugins {
    id("melodify.kmp.library")
    alias(libs.plugins.serialization)
}

kotlin {
    @Suppress("UnstableApiUsage")
    androidLibrary {
        namespace = "com.andannn.melodify.core.datastore"
    }

    sourceSets {
        commonMain.dependencies {
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.cio)
            implementation(libs.ktor.client.logging)
            implementation(libs.ktor.client.resources)
            implementation(libs.ktor.client.auth)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.serialization.kotlinx.json)
        }

        androidMain.dependencies {
            implementation(libs.ktor.client.okhttp)
            implementation(libs.jakewharton.timber)
        }
    }
}
