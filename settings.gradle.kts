pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "Secret Messaging"
include(":app")
include(":core:model")
include(":core:database")
include(":core:data")
include(":feature:keys")
include(":feature:exchange")
include(":core:crypto")
