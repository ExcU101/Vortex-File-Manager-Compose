plugins {
    id(Plugins.Library)
    kotlin(Plugins.Android)
}

android {
    namespace = "io.github.excu101.pluginsystem.common"
    compileSdk = AndroidConfigure.targetSdk

    defaultConfig {
        minSdk = AndroidConfigure.minSdk
        targetSdk = AndroidConfigure.targetSdk

        consumerProguardFiles("consumer-rules.pro")
    }
    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
}

dependencies {
    implementation(Deps.Compose.IconsExtended)
    implementation(Deps.Compose.Ui)
    implementation(Deps.Compose.Material)

    implementation(project(Deps.Application.PluginSystem))
    implementation(project(Deps.Application.FileSystemUnix))
    implementation(project(Deps.Application.FileSystem))
}