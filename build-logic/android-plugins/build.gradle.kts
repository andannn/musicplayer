plugins {
    `kotlin-dsl`
}

dependencies {
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.kotlin.gradlePlugin)
    compileOnly(libs.ksp.gradlePlugin)
}

gradlePlugin {
    plugins {
        register("androidApplication") {
            id = "musicplayer.android.application"
            implementationClass = "AndroidApplicationConventionPlugin"
        }
        register("androidLibrary") {
            id = "musicplayer.android.library"
            implementationClass = "AndroidLibraryConventionPlugin"
        }
        register("androidLibraryCompose") {
            id = "musicplayer.android.library.compose"
            implementationClass = "AndroidLibraryComposeConventionPlugin"
        }
        register("androidApplicationCompose") {
            id = "musicplayer.android.application.compose"
            implementationClass = "AndroidApplicationComposeConventionPlugin"
        }
        register("androidHilt") {
            id = "musicplayer.android.hilt"
            implementationClass = "AndroidHiltConventionPlugin"
        }
        register("androidTesting") {
            id = "musicplayer.android.testing"
            implementationClass = "AndroidTestingConventionPlugin"
        }
        register("androidRoom") {
            id = "musicplayer.android.room"
            implementationClass = "AndroidRoomConventionPlugin"
        }
        register("androidxSerialization") {
            id = "musicplayer.android.serialization"
            implementationClass = "AndroidSerializationPlugin"
        }
    }
}