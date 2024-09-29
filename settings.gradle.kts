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

rootProject.name = "Melodify"
include(":app")
include(":core:designsystem")
include(":core:domain")
include(":core:data")
include(":core:network")
include(":core:player")
include(":core:datastore")
include(":core:network")
include(":core:database")
include(":feature:common")
include(":feature:home")
include(":feature:player")
include(":feature:playlist")
