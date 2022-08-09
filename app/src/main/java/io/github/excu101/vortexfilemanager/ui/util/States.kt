package io.github.excu101.vortexfilemanager.ui.util

import androidx.compose.material.BottomDrawerState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
suspend fun BottomDrawerState.toggle() {
    if (isExpanded || isOpen) {
        close()
    } else {
        open()
    }
}

@OptIn(ExperimentalMaterialApi::class)
fun BottomDrawerState.toggle(scope: CoroutineScope) {
    scope.launch { toggle() }
}

@OptIn(ExperimentalMaterialApi::class)
suspend fun ModalBottomSheetState.toggle() {
    if (isVisible) {
        hide()
    } else {
        show()
    }
}

@OptIn(ExperimentalMaterialApi::class)
fun ModalBottomSheetState.toggle(scope: CoroutineScope) {
    scope.launch { toggle() }
}


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