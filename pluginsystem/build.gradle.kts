plugins {
    id("com.android.library")
    kotlin("android")
}

android {
    namespace = "io.github.excu101.pluginsystem"
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
    implementation(Deps.Compose.Ui)
    implementation(Deps.Compose.IconsCore)
    implementation(project(mapOf("path" to ":filesystem")))
    implementation(kotlin("reflect"))
}