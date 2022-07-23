plugins {
    id("com.android.library")
    kotlin("android")
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
    implementation(project(mapOf("path" to ":pluginsystem")))
    implementation(Deps.Compose.IconsExtended)
    implementation(project(mapOf("path" to ":filesystem-unix")))
    implementation(project(mapOf("path" to ":filesystem")))
}