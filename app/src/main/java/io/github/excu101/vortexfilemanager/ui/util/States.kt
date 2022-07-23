package io.github.excu101.vortexfilemanager.ui.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable

@Composable
fun <T> stateOf(value: T) = remember {
    mutableStateOf(value)
}

@Composable
fun <T> derivedOf(value: T) = remember {
    derivedStateOf { value }
}

@Composable
fun <T> stateOfSavable(value: T) = rememberSaveable {
    mutableStateOf(value)
}

@Composable
fun <T> derivedOfSavable(value: T) = rememberSaveable {
    derivedStateOf { value }
}