plugins {
    id(Plugins.Application) version (Versions.androidVer) apply false
    id(Plugins.Library) version (Versions.androidVer) apply false
    id(Plugins.Test) version (Versions.androidVer) apply false
    id(Plugins.KotlinAndroid) version (Versions.kotlinVer) apply false
    id(Plugins.Hilt) version (Versions.hiltVer) apply false
    id(Plugins.Detekt) version (Versions.detektVer) apply false
}

subprojects {
    val args = listOf(
        "-opt-in=kotlin.RequiresOptIn",
        "-Xcontext-receivers",
    )

    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>() {
        kotlinOptions.jvmTarget = "1.8"
        kotlinOptions.freeCompilerArgs = args
    }
}