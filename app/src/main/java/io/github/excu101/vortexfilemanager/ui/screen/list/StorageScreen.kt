package io.github.excu101.vortexfilemanager.ui.screen.list

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.app.Activity
import android.os.Environment
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.accompanist.systemuicontroller.SystemUiController
import io.github.excu101.filesystem.fs.utils.asPath
import io.github.excu101.pluginsystem.model.Action
import io.github.excu101.pluginsystem.model.GroupAction
import io.github.excu101.ui.component.layout.ErrorMessage
import io.github.excu101.ui.component.layout.ProgressBarView
import io.github.excu101.ui.component.layout.SelectableSectionList
import io.github.excu101.ui.component.text.Subtitle
import io.github.excu101.vortexfilemanager.base.utils.collectAsEffect
import io.github.excu101.vortexfilemanager.base.utils.collectAsState
import io.github.excu101.vortexfilemanager.data.intent.SideEffect.Empty
import io.github.excu101.vortexfilemanager.data.intent.SideEffect.Message
import io.github.excu101.vortexfilemanager.data.intent.StorageDialogState.*
import io.github.excu101.vortexfilemanager.provider.ScopedStorageContract
import io.github.excu101.vortexfilemanager.ui.MainScreenController
import io.github.excu101.vortexfilemanager.ui.screen.list.view.*
import io.github.excu101.vortexfilemanager.ui.util.toggle
import io.github.excu101.vortexfilemanager.ui.view.action.Defaults
import io.github.excu101.vortexfilemanager.ui.view.action.MenuAction
import io.github.excu101.vortexfilemanager.ui.view.action.Ordering
import io.github.excu101.vortexfilemanager.ui.view.action.asGroup
import io.github.excu101.vortexfilemanager.ui.view.icon.MenuIconState
import io.github.excu101.vortexfilemanager.ui.view.trail.TrailRow
import io.github.excu101.vortexfilemanager.util.drawerColors
import io.github.excu101.vortexfilemanager.util.item
import io.github.excu101.vortexfilemanager.util.listBuilder
import io.github.excu101.vortexfilemanager.util.progressBarColors
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
                    viewModel.navigateTo()
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
                viewModel.navigateTo()
            }
        }
    )

    val effect by viewModel.collectAsEffect(initial = Empty)
    val state by viewModel.collectAsState()
    val trail by viewModel.trail.collectAsState()
    val dialog by viewModel.dialog.collectAsState()
    val selected by viewModel.selected.collectAsState()

    var isBarHide by remember { controller.bar.isHide }

    val bottom = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)

    var sections by remember {
        mutableStateOf(listBuilder {
            item(GroupAction(
                name = "Main",
                actions = listBuilder {
                    item(title = "Add", icon = Icons.Outlined.Add)
                    item(title = "Delete", icon = Icons.Outlined.Delete)
                }
            ))
        })
    }

    ModalBottomSheetLayout(
        sheetState = bottom,
        sheetContent = {
            SelectableSectionList(
                colors = drawerColors(),
                sections = sections,
                onActionClick = { action ->
                    controller.notifyActionListeners(action = action)
                },
                selectedActions = listOf()
            )
        },
    ) {
        StorageScaffold(
            trail = {
                val segments = trail.segments
                val selectedSegment = trail.selected

                Column {
                    TrailRow(
                        segments = segments,
                        currentSelected = selectedSegment,
                        onTrailClick = { viewModel.navigateTo(it) }
                    )
                    if (trail.currentSelected.isDirectory) {
                        Subtitle(
                            modifier = Modifier.padding(16.dp),
                            text = trail.currentSelected.properties.count.toString()
                        )
                    }
                }
            },
            additional = { },
        ) {
            val scroller = rememberLazyListState()
            val isScrolling by remember {
                derivedStateOf { scroller.isScrollInProgress }
            }
            isBarHide = isScrolling

            StorageList(
                data = state.data,
                selected = selected,
                state = scroller,
                onSelect = { model ->
                    viewModel.choose(listOf(model))
                },
                onItemClick = { model ->
                    viewModel.navigateTo(model)
                },
                isLightModeEnabled = true
            )

            if (state.error != null) {
                ErrorMessage(
                    modifier = Modifier.fillMaxSize(),
                    message = state.error!!.message.toString()
                )
            }

            if (state.requiresPermission) {
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

            if (state.requiresSpecialPermission) {
                SpecialPermWarning(
                    modifier = Modifier.fillMaxSize(),
                    onActionClick = {
                        contractSpecial.launch()
                    }
                )
            }

            if (state.isLoading) {
                ProgressBarView(
                    modifier = Modifier.fillMaxSize(),
                    message = state.loadingMessage,
                    textButton = "Cancel",
                    colors = progressBarColors()
                )
            }

        }
    }

    when (val it = dialog) {
        StorageCreateDialog -> {
            FileCreateDialog(
                onDismissRequest = {
                    viewModel.hideDialog()
                },
                onCancelRequest = {
                    viewModel.hideDialog()
                },
                onConfirmRequest = { isDirectory, path ->
                    if (isDirectory) {
                        viewModel.createDirectory(path)
                    } else {
                        viewModel.createFile(path)
                    }
                }
            )
        }

        is StorageWarningDialog -> {
            FileListWarningDialog(
                message = it.message,
                onDismissRequest = {
                    viewModel.hideDialog()
                },
                onCancelRequest = {
                    viewModel.hideDialog()
                },
                onConfirmRequest = {

                }
            )
        }
        StorageEmptyDialog -> {}
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

        Empty -> {

        }
    }

    LaunchedEffect(
        key1 = Unit,
        block = {
            if (!controller.bar.actions.value.contains(
                    Action(
                        title = "Sort",
                        icon = Icons.Outlined.Sort
                    )
                )
            ) {
                controller.bar.actions.value += Action(
                    title = "Sort",
                    icon = Icons.Outlined.Sort
                )
            }
            if (!controller.bar.actions.value.contains(
                    Action(
                        title = "More options",
                        icon = Icons.Outlined.MoreVert
                    )
                )
            ) {
                controller.bar.actions.value += Action(
                    title = "More options",
                    icon = Icons.Outlined.MoreVert
                )
            }
        }
    )

    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(
        key1 = lifecycleOwner,
        effect = {
            controller.subscribeOnAction(index = 1) {
                when (it.title) {
                    "Back to parent" -> viewModel.navigateToParent()

                    "Select all" -> viewModel.selectAll()

                    "Open folder" -> {
                        viewModel.openFolder()
                        scope.launch { bottom.hide() }
                    }

                    "Deselect all" -> {
                        viewModel.deselectAll()
                    }

                    "Add" -> {
                        viewModel.createDialog()
                    }

                    "Sort" -> {
                        sections = Ordering.asGroup()
                        bottom.toggle(scope = scope)
                    }

                    "Add new file" -> {
                        viewModel.createDialog()
                        scope.launch { bottom.hide() }
                    }

                    "More options" -> {
                        sections = Defaults(selected = selected.toTypedArray()).asGroup()
                        bottom.toggle(scope = scope)
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