package io.github.excu101.vortexfilemanager.ui.screen.list

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.app.Activity
import android.os.Environment
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.SnackbarResult
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material.icons.outlined.Sort
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.accompanist.systemuicontroller.SystemUiController
import io.github.excu101.filesystem.fs.utils.asPath
import io.github.excu101.pluginsystem.model.Action
import io.github.excu101.ui.component.layout.ErrorMessage
import io.github.excu101.ui.component.layout.ProgressBarView
import io.github.excu101.vortexfilemanager.base.utils.collectAsEffect
import io.github.excu101.vortexfilemanager.base.utils.collectAsState
import io.github.excu101.vortexfilemanager.data.fileModelSetOf
import io.github.excu101.vortexfilemanager.data.intent.Contracts.SideEffect.*
import io.github.excu101.vortexfilemanager.provider.ScopedStorageContract
import io.github.excu101.vortexfilemanager.ui.MainScreenController
import io.github.excu101.vortexfilemanager.ui.screen.list.view.PermWarning
import io.github.excu101.vortexfilemanager.ui.screen.list.view.SpecialPermWarning
import io.github.excu101.vortexfilemanager.ui.screen.list.view.StorageList
import io.github.excu101.vortexfilemanager.ui.screen.list.view.StorageScaffold
import io.github.excu101.vortexfilemanager.ui.theme.Theme
import io.github.excu101.vortexfilemanager.ui.theme.key.layoutProgressActionTintColorKey
import io.github.excu101.vortexfilemanager.ui.theme.key.layoutProgressBarBackgroundColorKey
import io.github.excu101.vortexfilemanager.ui.theme.key.layoutProgressBarTintColorKey
import io.github.excu101.vortexfilemanager.ui.theme.key.layoutProgressTitleTextColorKey
import io.github.excu101.vortexfilemanager.ui.view.action.MenuAction
import io.github.excu101.vortexfilemanager.ui.view.icon.MenuIconState
import io.github.excu101.vortexfilemanager.ui.view.trail.TrailRow
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun StorageScreen(
    uiController: SystemUiController,
    navigator: NavController,
    controller: MainScreenController,
    viewModel: StorageViewModel = hiltViewModel(),
) {
    val scope = rememberCoroutineScope()

    val contract = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = { perms ->
            perms.forEach { (_, granted) ->
                if (!granted) {
                    viewModel.launch()
                } else {
                    viewModel.navigateTo(viewModel.currentPath.value)
                }
            }
        }
    )

    val contractSpecial = rememberLauncherForActivityResult(
        contract = ScopedStorageContract,
        onResult = { result ->
            if (!result) {
                viewModel.launch()
            } else {
                viewModel.navigateTo(viewModel.currentPath.value)
            }
        }
    )

    val effect by viewModel.collectAsEffect(initial = Empty)
    val state by viewModel.collectAsState()
    val operation by viewModel.currentOperation.collectAsState()
    val bottomState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    val selected = viewModel.selected
    val trail by viewModel.trail.collectAsState()

    Crossfade(targetState = state) {
        StorageScaffold(
            targetState = state,
            trail = {
                val segments = trail.segments
                val selectedSegment = trail.selected
                TrailRow(
                    segments = segments,
                    currentSelected = selectedSegment
                ) { model ->
                    viewModel.navigateTo(model)
                }
            },
            additional = { },
            dialog = { }
        ) {
            val data by remember {
                derivedStateOf { it.data }
            }

            StorageList(
                data = data,
                selected = selected,
                onIconClick = { model ->
                    viewModel.choose(fileModelSetOf(model))
                },
                onItemClick = { model ->
                    viewModel.navigateTo(model)
                }
            )

            if (it.isLoading) {
                ProgressBarView(
                    modifier = Modifier.fillMaxSize(),
                    message = it.loadingMessage,
                    textButton = "Cancel",
                    messageColor = Theme[layoutProgressTitleTextColorKey],
                    backgroundColor = Theme[layoutProgressBarBackgroundColorKey],
                    buttonTextColor = Theme[layoutProgressActionTintColorKey],
                    progressColor = Theme[layoutProgressBarTintColorKey]
                )
            }

            if (it.error != null) {
                ErrorMessage(
                    modifier = Modifier.fillMaxSize(),
                    message = it.error.message.toString()
                )
            }

            if (it.requiresPermission) {
                PermWarning(
                    modifier = Modifier.fillMaxSize(),
                    onActionClick = {
                        contract.launch(
                            arrayOf(
                                READ_EXTERNAL_STORAGE,
                                WRITE_EXTERNAL_STORAGE
                            )
                        )
                    },
                )
            }

            if (it.requiresSpecialPermission) {
                SpecialPermWarning(
                    modifier = Modifier.fillMaxSize(),
                    onActionClick = {
                        contractSpecial.launch()
                    }
                )
            }
        }
    }

    LaunchedEffect(
        key1 = controller.bar.isContextual,
        block = {
            if (controller.bar.isContextual.value) {
                controller.navigationIconState = MenuIconState.CLOSE
                controller.bar.navigationAction.value =
                    Action(title = "Close contextual menu", icon = Icons.Outlined.Close)
            } else {
                controller.navigationIconState = MenuIconState.MENU
                controller.bar.navigationAction.value = MenuAction
            }
        }
    )

    when (effect) {
        is Message -> {
            scope.launch {
                val result = controller.showSnackbar(
                    message = (effect as Message).text,
                    actionText = (effect as Message).labelAction
                )

                when (result) {
                    SnackbarResult.Dismissed -> {

                    }

                    SnackbarResult.ActionPerformed -> {
                        (effect as Message).action?.invoke()
                    }
                }
            }

        }

        is Empty -> {

        }

        is DialogEmpty -> {

        }

        is DialogWarning -> {

        }

        is DialogCreate -> {

        }

        is ModelInfo -> {

        }
    }

    LaunchedEffect(
        key1 = Unit,
        block = {
            controller.bar.actions.value += Action(
                title = "Sort file list",
                icon = Icons.Outlined.Sort
            )

            controller.bar.actions.value += Action(
                title = "More options",
                icon = Icons.Outlined.MoreVert
            )
        }
    )

    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(
        key1 = lifecycleOwner,
        effect = {
            controller.subscribeOnAction(index = 1) {
                when (it.title) {
                    "Back to parent" -> {
                        viewModel.navigateToParent()
                    }

                    "Select all" -> {
                        viewModel.selectAll()
                    }

                    "Deselect all" -> {
                        viewModel.deselectAll()
                    }

                    "Add some content" -> {
                        viewModel.showDialog(DialogCreate)
                    }

                    "Add new file" -> {
                        viewModel.showDialog(DialogCreate)
                    }

                    "More options" -> {
                        scope.launch { bottomState.show() }
                    }

                    "Delete" -> {
                        viewModel.delete()
                    }

                    else -> {}
                }
            }
            onDispose {
                controller.unsubscribeOnAction(index = 1)
            }
        }
    )

    val context = LocalContext.current
    BackHandler {
        if (controller.isDrawerVisible) {
            controller.hideDrawer(scope)
            return@BackHandler
        }

        if (trail.currentSelected.path != Environment.getExternalStorageDirectory().asPath()) {
            return@BackHandler viewModel.navigateToParent()
        }

        (context as? Activity)?.finish()
    }
}