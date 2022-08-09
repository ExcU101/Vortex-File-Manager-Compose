package io.github.excu101.vortexfilemanager.ui.theme

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.tween
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import io.github.excu101.ui.component.icon.Unspecified

object Theme {

    val systemTheme: Theme
        @Composable
        get() {
            if (isSystemInDarkTheme()) dark else light
            return this
        }

    val light by lazy {
        isDark = false
    }

    val dark by lazy {
        isDark = true
    }

    private val icons: HashMap<String, ImageVector> = hashMapOf()

    private val text: HashMap<String, String> = hashMapOf()

    private val colors: HashMap<String, Color> = hashMapOf()

    private val dimens: HashMap<String, Dp> = hashMapOf()

    var isDark: Boolean = false
        set(value) {
            update()
            field = value
        }

    fun getText(key: String): String = text[key] ?: String(byteArrayOf())

    inline fun getText(key: () -> String): String = getText(key = key())

    fun getColor(key: String): Color = colors[key] ?: Color.Unspecified

    inline fun getColor(key: () -> String): Color = getColor(key = key())

    fun getDp(key: String): Dp {
        return dimens[key] ?: Dp.Unspecified
    }

    inline fun getDp(key: () -> String): Dp {
        return getDp(key = key())
    }

    fun getIcon(key: String): ImageVector = icons[key] ?: Unspecified

    inline fun getIcon(key: () -> String): ImageVector = getIcon(key())

    inline operator fun <reified T : Any> get(key: String): T {
        val value = when (T::class) {
            Color::class -> getColor(key = key)
            Dp::class -> getDp(key = key)
            String::class -> getText(key = key)
            ImageVector::class -> getIcon(key = key)
            else -> throw Throwable("Unsupported type")
        }
        return value as T
    }

    inline operator fun <reified T : Any> get(key: () -> String): T = get(key())

    operator fun set(key: String, value: Color) {
        colors[key] = value
    }

    operator fun set(key: String, value: Dp) {
        dimens[key] = value
    }

    operator fun set(key: String, value: String) {
        text[key] = value
    }

    operator fun set(key: String, value: ImageVector) {
        icons[key] = value
    }

    init {
        update()
    }

    fun update() {
        if (isDark) defaultDarkTheme() else defaultLightTheme()
        defaultTexts()
    }
}

fun getText(key: String) = Theme.get<String>(key = key)

@Suppress(names = ["FunctionName"])
inline fun <reified T : Any> Theme(block: () -> String) = Theme.get<T>(block)

@Composable
inline fun themedColorAnimation(
    animationSpec: AnimationSpec<Color> = tween(durationMillis = 300),
    block: () -> String,
) = themedColorAnimation(key = block(), animationSpec)

@Composable
fun themedColorAnimation(
    key: String,
    animationSpec: AnimationSpec<Color> = tween(durationMillis = 300)
) = fastChangeColorAnim(Theme[key], animationSpec)

@Composable
fun fastChangeColorAnim(
    targetValue: Color,
    animationSpec: AnimationSpec<Color> = tween(durationMillis = 300)
) = animateColorAsState(
    targetValue = targetValue,
    animationSpec = animationSpec
).value
