plugins {
    id(Plugins.Library)
    kotlin(Plugins.Android)
}

android {
    namespace = "io.github.excu101.ui.component"
    compileSdk = AndroidConfigure.targetSdk

    defaultConfig {
        minSdk = AndroidConfigure.minSdk
        targetSdk = AndroidConfigure.targetSdk
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = Versions.composeCompilerVer
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
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    implementation(Deps.Compose.Ui)
    implementation(Deps.Compose.Runtime)
    implementation(Deps.Compose.Material)
    implementation(Deps.Compose.IconsCore)
    implementation(Deps.Compose.IconsExtended)

    debugImplementation(Deps.Compose.Tooling)
    implementation(Deps.Compose.ToolingPreview)
}