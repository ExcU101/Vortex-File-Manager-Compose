object Deps {

    // AndroidX
    object AndroidX {
        const val DataStorePreferences = "androidx.datastore:datastore-preferences:1.0.0"
        const val Multidex = "androidx.multidex:multidex:${Versions.multidexVer}"
        const val Core = "androidx.core:core-ktx:${Versions.coreVer}"
        const val Collection = "androidx.collection:collection-ktx:${Versions.collectionVer}"
    }

    object Lottie {
        const val Compose = "com.airbnb.android:lottie-compose:4.0.0"
    }

    // Jetpack Compose
    object Compose {
        const val Activity = "androidx.activity:activity-compose:1.6.0-alpha05"

        const val Compiler = "androidx.compose.compiler:compiler:${Versions.composeCompilerVer}"
        const val Graphics = "androidx.compose.animation:animation-graphics:${Versions.composeVer}"

        const val Material = "androidx.compose.material:material:${Versions.composeVer}"
        const val Material3 = "androidx.compose.material3:material3:${Versions.composeMaterial3Ver}"
        const val Navigation = "androidx.navigation:navigation-compose:${Versions.navigation}"

        const val Ui = "androidx.compose.ui:ui:${Versions.composeVer}"
        const val UiUtil = "androidx.compose.ui:ui-util:${Versions.composeVer}"

        const val Tooling = "androidx.compose.ui:ui-tooling:${Versions.composeVer}"
        const val ToolingPreview = "androidx.compose.ui:ui-tooling-preview:${Versions.composeVer}"

        const val Runtime = "androidx.compose.runtime:runtime:${Versions.composeVer}"

        const val Livedata = "androidx.compose.runtime:runtime-livedata:${Versions.composeVer}"
        const val Font = "androidx.compose.ui:ui-text-google-fonts:${Versions.composeVer}"

        const val IconsCore =
            "androidx.compose.material:material-icons-core:${Versions.composeVer}"
        const val IconsExtended =
            "androidx.compose.material:material-icons-extended:${Versions.composeVer}"
    }

    // Threads
    object Coroutines {
        const val Core = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.coroutinesVer}"
        const val Android =
            "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.coroutinesVer}"
        const val Test = "org.jetbrains.kotlinx:kotlinx-coroutines-test:${Versions.coroutinesVer}"
    }

    object Hilt {
        const val Android = "com.google.dagger:hilt-android:${Versions.hiltVer}"
        const val NavigationCompose = "androidx.hilt:hilt-navigation-compose:1.0.0"
        const val Compiler = "com.google.dagger:hilt-compiler:${Versions.hiltVer}"
    }

    // AndroidX Lifecycle
    object Lifecycle {
        const val LiveData = "androidx.lifecycle:lifecycle-livedata-ktx:${Versions.lifecycleVer}"
        const val Runtime = "androidx.lifecycle:lifecycle-runtime-ktx:${Versions.lifecycleVer}"
        const val ViewModel = "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.lifecycleVer}"
        const val ViewModelCompose =
            "androidx.lifecycle:lifecycle-viewmodel-compose:${Versions.lifecycleVer}"
        const val SavedStateViewModel =
            "androidx.lifecycle:lifecycle-viewmodel-savedstate:${Versions.lifecycleVer}"
    }

    // Compose Accompanist
    object Accompanist {
        const val Drawable: String =
            "com.google.accompanist:accompanist-drawablepainter:${Versions.accompanistVer}"

        const val SwipeToRef =
            "com.google.accompanist:accompanist-swiperefresh:${Versions.accompanistVer}"

        const val NavigationMaterial =
            "com.google.accompanist:accompanist-navigation-material:${Versions.accompanistVer}"

        const val SystemUi =
            "com.google.accompanist:accompanist-systemuicontroller:${Versions.accompanistVer}"

        const val Permissions =
            "com.google.accompanist:accompanist-permissions:${Versions.accompanistVer}"

        const val NavigationAnimation =
            "com.google.accompanist:accompanist-navigation-animation:${Versions.accompanistVer}"
    }

    object Application {
        val FileSystem = mapOf("path" to ":filesystem")
        val FileSystemUnix = mapOf("path" to ":filesystem-unix")
        val PluginSystem = mapOf("path" to ":pluginsystem")
        val PluginSystemCommon = mapOf("path" to ":pluginsystem-common")
        val UiComponent = mapOf("path" to ":ui-component")
    }

    object UnitTest {
        const val JUnit = "org.junit.jupiter:junit-jupiter:${Versions.junitVer}"
        const val Mockk = "io.mockk:mockk:${Versions.mockkVersion}"
        const val MockkJvm = "io.mockk:mockk-agent-jvm:${Versions.mockkVersion}"
        const val MockkAndroid = "io.mockk:mockk-android:${Versions.mockkVersion}"
    }

    object Architecture {

    }

}