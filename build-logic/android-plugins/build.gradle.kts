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
        register("kotlinMultiplatformLibrary") {
            id = "melodify.kmp.library"
            implementationClass = "KMPLibraryConventionPlugin"
        }
        register("kotlinMultiplatformApplication") {
            id = "melodify.kmp.application"
            implementationClass = "KMPApplicationConventionPlugin"
        }
    }
}