package io.github.excu101.ui.component.utils

import androidx.compose.runtime.Composable

typealias Content = @Composable () -> Unit

typealias SpecificContent<T> = @Composable (T) -> Unit