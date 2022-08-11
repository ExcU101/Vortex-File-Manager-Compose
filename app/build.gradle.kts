import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    id(Plugins.Application)
    id(Plugins.Parcelize)
    id(Plugins.HiltPlugin)
    kotlin(Plugins.Android)
    kotlin(Plugins.Kapt)
}

kapt {
    correctErrorTypes = true
}

android {
    signingConfigs {
        create(BuildConfig.Release.Name) {
            val props = gradleLocalProperties(rootDir)
            storeFile = file(props["signing.path"].toString())
            storePassword = props["signing.pathPassword"].toString()
            keyAlias = props["signing.alias"].toString()
            keyPassword = props["signing.aliasPassword"].toString()
        }
    }
    namespace = AndroidConfigure.applicationId
    compileSdk = AndroidConfigure.targetSdk

    defaultConfig {
        applicationId = AndroidConfigure.applicationId
        minSdk = AndroidConfigure.minSdk
        targetSdk = AndroidConfigure.targetSdk
        versionCode = AndroidConfigure.versionCode
        versionName = AndroidConfigure.versionName
        multiDexEnabled = AndroidConfigure.multiDexEnabled

        vectorDrawables {
            useSupportLibrary = true
        }
        externalNativeBuild {
            cmake {
                cppFlags += ""
            }
        }
    }

    buildTypes {
        getByName(BuildConfig.Release.Name) {
            isShrinkResources = true
            isMinifyEnabled = true
            multiDexKeepProguard = file(path = "proguard-rules.pro")
            proguardFiles(
                files = arrayOf(
                    getDefaultProguardFile(name = "proguard-android-optimize.txt"),
                    "proguard-rules.pro"
                )
            )
            signingConfig = signingConfigs.getByName("release")
        }
        create("benchmark") {
            initWith(getByName("release"))
            signingConfig = signingConfigs.getByName("debug")
            matchingFallbacks += listOf("release")
            isDebuggable = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    java {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = Versions.composeCompilerVer
    }
    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    externalNativeBuild {
        cmake {
            path = file(BuildConfig.CMake.Path)
            version = Versions.cmakeVer
        }
    }
}

dependencies {
    implementation(Deps.Hilt.Android)
    implementation(Deps.Hilt.NavigationCompose)
    kapt(Deps.Hilt.Compiler)

    implementation(Deps.AndroidX.Multidex)
    implementation(Deps.AndroidX.Collection)
    implementation(Deps.AndroidX.Core)
    implementation(Deps.AndroidX.DataStorePreferences)
    implementation("androidx.profileinstaller:profileinstaller:1.2.0")

    implementation(Deps.Lottie.Compose)

    implementation(Deps.Coroutines.Core)
    implementation(Deps.Coroutines.Android)

    implementation(Deps.Lifecycle.Runtime)
    implementation(Deps.Lifecycle.RuntimeCompose)
    implementation(Deps.Lifecycle.LiveData)
    implementation(Deps.Lifecycle.ViewModel)
    implementation(Deps.Lifecycle.ViewModelCompose)
    implementation(Deps.Lifecycle.SavedStateViewModel)

    implementation(Deps.Compose.Activity)
    implementation(Deps.Compose.Graphics)

    implementation(Deps.Compose.Navigation)
    implementation(Deps.Compose.Ui)
    implementation(Deps.Compose.UiUtil)
    implementation(Deps.Compose.Material)
    implementation(Deps.Compose.Runtime)
    implementation(Deps.Compose.Compiler)
    implementation(Deps.Compose.Livedata)
    implementation(Deps.Compose.IconsCore)
    implementation(Deps.Compose.IconsExtended)
    implementation(Deps.Compose.Font)

    debugImplementation(Deps.Compose.Tooling)
    implementation(Deps.Compose.ToolingPreview)

    implementation(Deps.Accompanist.SystemUi)
    implementation(Deps.Accompanist.SwipeToRef)
    implementation(Deps.Accompanist.Permissions)
    implementation(Deps.Accompanist.Drawable)
    implementation(Deps.Accompanist.NavigationAnimation)
    implementation(Deps.Accompanist.NavigationMaterial)

    implementation(project(Deps.Application.FileSystem))
    implementation(project(Deps.Application.FileSystemUnix))
    implementation(project(Deps.Application.PluginSystem))
    implementation(project(Deps.Application.PluginSystemCommon))
    implementation(project(Deps.Application.UiComponent))

    testImplementation(Deps.UnitTest.JUnit)

    testImplementation(Deps.Coroutines.Test)

    testImplementation(Deps.UnitTest.Mockk)
    androidTestImplementation(Deps.UnitTest.MockkAndroid)
}