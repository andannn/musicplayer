pluginManagement {
    includeBuild("build-logic")
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}
dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "MusicPlayer"
include(":app")
include(":feature:home")
include(":feature:library")
include(":feature:player")
include(":feature:playlist")
include(":feature:playqueue")
include(":core:designsystem")
include(":core:mediastore")
include(":core:data")
include(":core:player")
include(":core:database")
include(":core:datastore")
