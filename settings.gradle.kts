pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
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
include(":core:model")
include(":core:player")
include(":core:database")
include(":core:datastore")
include(":datastore")
