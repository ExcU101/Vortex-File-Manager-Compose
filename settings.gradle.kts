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
rootProject.name = "Vortex File Manager"
include(
    ":app",
    ":benchmark",
    ":filesystem",
    ":filesystem-unix",
    ":pluginsystem",
    ":pluginsystem-common",
    ":manifest-dsl",
    ":ui-component"
)