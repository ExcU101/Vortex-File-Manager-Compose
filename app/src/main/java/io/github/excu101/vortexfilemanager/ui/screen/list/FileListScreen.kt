package io.github.excu101.vortexfilemanager.ui.screen.list

import android.Manifest
import android.app.Activity
import android.os.Environment
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityOptionsCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.accompanist.systemuicontroller.SystemUiController
import io.github.excu101.filesystem.fs.utils.asPath
import io.github.excu101.pluginsystem.model.Action
import io.github.excu101.pluginsystem.provider.ActionManager
import io.github.excu101.ui.component.text.Subtitle
import io.github.excu101.ui.component.text.SubtitleTextStyle
import io.github.excu101.vortexfilemanager.base.utils.collectAsEffect
import io.github.excu101.vortexfilemanager.base.utils.collectAsState
import io.github.excu101.vortexfilemanager.data.FileModel
import io.github.excu101.vortexfilemanager.data.intent.Contracts
import io.github.excu101.vortexfilemanager.data.intent.Contracts.SideEffect.*
import io.github.excu101.vortexfilemanager.data.intent.Contracts.State.FileList
import io.github.excu101.vortexfilemanager.data.intent.Contracts.State.Loading
import io.github.excu101.vortexfilemanager.provider.ScopedStorageContract
import io.github.excu101.vortexfilemanager.ui.MainScreenController
import io.github.excu101.vortexfilemanager.ui.screen.list.view.*
import io.github.excu101.vortexfilemanager.ui.theme.Theme
import io.github.excu101.vortexfilemanager.ui.theme.getText
import io.github.excu101.vortexfilemanager.ui.theme.key.fileListFilesCountKey
import io.github.excu101.vortexfilemanager.ui.theme.key.trailItemTitleTextColorKey
import io.github.excu101.vortexfilemanager.ui.util.ProgressBarView
import io.github.excu101.vortexfilemanager.ui.view.action.MenuAction
import io.github.excu101.vortexfilemanager.ui.view.additional.AdditionalMenu
import io.github.excu101.vortexfilemanager.ui.view.additional.AdditionalMenuHost
import io.github.excu101.vortexfilemanager.ui.view.additional.rememberAdditionalMenuController
import io.github.excu101.vortexfilemanager.ui.view.icon.MenuIconState
import io.github.excu101.vortexfilemanager.ui.view.list.SectionList
import io.github.excu101.vortexfilemanager.ui.view.trail.TrailRow
import io.github.excu101.vortexfilemanager.util.item
import io.github.excu101.vortexfilemanager.util.listBuilder
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun FileListScreen(
    uiController: SystemUiController,
    navigator: NavController,
    controller: MainScreenController,
    viewModel: FileListViewModel = hiltViewModel(),
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
    val state by viewModel.collectAsState(initial = Loading(text = "Getting content..."))
    val operation by viewModel.currentOperation.collectAsState()
    val selectedFiles by viewModel.selectedFiles.collectAsState()
    val bottomState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    val selectedCount = selectedFiles.size
    val trail by viewModel.trail.collectAsState()
    var showsDialog by remember { mutableStateOf(false) }
    val scrollUp by viewModel.scrollUp.collectAsState()
    controller.bar.isHide.value = scrollUp

    val menuController = rememberAdditionalMenuController()

    ModalBottomSheetLayout(
        sheetState = bottomState,
        sheetContent = {
            SectionList(
                sections = listBuilder {
                    if (selectedFiles.isNotEmpty()) {
                        ActionManager.groups.forEach {
                            item(it.name) {
                                items(it.actions)
                            }
                        }
                        item("Additional") {
                            item(title = "Deselect all", Icons.Outlined.Deselect)
                        }
                    } else {
                        item("Available") {
                            item(title = "Select all", Icons.Outlined.SelectAll)
                        }

                    }
                },
                onActionClick = {
                    controller.notifyActionListeners(action = it)
                }
            )
        }
    ) {
        FileListScreenScaffold(
            targetState = state,
            trail = {
                val segments = trail.segments
                val selected = trail.selected
                val current = FileModel(trail.currentSelected)

                Column {
                    TrailRow(
                        segments = segments,
                        currentSelected = selected,
                    ) { path ->
                        viewModel.navigateTo(FileModel(path))
                    }
                    Subtitle(
                        text = if (current.isDirectory) {
                            getText(fileListFilesCountKey).replace(
                                oldValue = "%s",
                                newValue = current.properties.files.size.toString()
                            ) + " Folders: ${current.properties.dirs.size}"
                        } else {
                            "Size: ${current.size}" + if (current.mimeType.type.isNotEmpty()) " MimeType: ${current.mimeType.type}" else ""
                        } + if (selectedFiles.isNotEmpty()) " (Selected: ${selectedCount})" else "",
                        style = SubtitleTextStyle.copy(
                            color = Theme[trailItemTitleTextColorKey],
                        ),
                        modifier = Modifier.padding(16.dp)
                    )
                    Divider()
                }

            },
            dialog = {
                when (val dialogType = effect) {
                    DialogCreate -> {
                        FileCreateDialog(
                            shows = showsDialog,
                            onDismissRequest = {
                                viewModel.hideDialog()
                            },
                            onCancelRequest = {
                                viewModel.hideDialog()
                            },
                            onConfirmRequest = { isDirectory, path ->
                                if (isDirectory) {
                                    viewModel.createDirectory(
                                        dest = trail.currentSelected.resolve(
                                            other = path
                                        )
                                    )
                                } else {
                                    viewModel.createFile(
                                        dest = trail.currentSelected.resolve(
                                            other = path
                                        )
                                    )
                                }
                                viewModel.hideDialog()
                            }
                        )
                    }
                    is DialogWarning -> {
                        FileListWarningDialog(
                            shows = showsDialog,
                            message = dialogType.message,
                            onDismissRequest = {
                                viewModel.hideDialog()
                            },
                            onConfirmRequest = {
                                viewModel.hideDialog()
                            },
                            onCancelRequest = {
                                viewModel.hideDialog()
                            }
                        )
                    }
                    else -> {}
                }

            },
            additional = {
                AdditionalMenuHost(
                    controller = menuController,
                    onIconCloseClick = {
                        viewModel.deselectAll()
                        menuController.hide()
                    },
                    content = { message, closeListener, actions ->
                        AdditionalMenu(
                            text = message,
                            onIconCloseClick = closeListener,
                            actions = actions
                        ) { action ->
                            controller.notifyActionListeners(action)
                        }
                    }
                )
            },
            content = {
                when (it) {
                    is FileList -> {
                        val scroller = rememberLazyListState()
                        val visible by remember {
                            derivedStateOf { scroller.firstVisibleItemIndex }
                        }
                        viewModel.updateScrollPosition(visible)

                        FileListView(
                            state = scroller,
                            data = it.data,
                            selected = selectedFiles,
                            onItemClick = { model ->
                                viewModel.navigateTo(model = model)
                            },
                            onIconClick = { model ->
                                viewModel choose model
                            },
                        )
                    }

                    is Loading -> {
                        ProgressBarView(
                            modifier = Modifier.fillMaxSize(),
                            text = it.text,
                            textButton = "Cancel",
                        )
                    }

                    is Contracts.State.RequiresPerm -> {
                        PermWarning(
                            modifier = Modifier.fillMaxSize(),
                            onActionClick = {
                                contract.launch(
                                    arrayOf(
                                        Manifest.permission.READ_EXTERNAL_STORAGE,
                                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                                    )
                                )
                            },
                        )
                    }

                    is Contracts.State.RequiresFullStoragePerm -> {
                        val view = LocalView.current
                        SpecialPermWarning(
                            modifier = Modifier.fillMaxSize(),
                            onActionClick = {
                                contractSpecial.launch(
                                    options = ActivityOptionsCompat.makeClipRevealAnimation(
                                        view,
                                        view.width / 2,
                                        view.height / 2,
                                        0,
                                        0
                                    )
                                )
                            }
                        )
                    }

                    is Contracts.State.Warning -> {
                        FileWarningView(
                            modifier = Modifier.fillMaxSize(),
                            icon = it.icon,
                            text = it.message,
                            actions = it.actions
                        ) { action ->
                            controller.notifyActionListeners(action = action)
                        }
                    }

                    is Contracts.State.CriticalError -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center,
                            content = { Text(text = it.error.stackTraceToString()) }
                        )
                    }
                }
            }
        )
    }



    LaunchedEffect(
        key1 = operation,
        block = {
            if (operation != null) {
                menuController.show()
                menuController.message = operation!!.progress
            } else {
                menuController.hide()
            }
        }
    )

    LaunchedEffect(
        key1 = selectedFiles,
        block = {
            controller.bar.isContextual.value = selectedFiles.isNotEmpty()
        }
    )

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

    LaunchedEffect(
        key1 = effect,
        block = {
            when (effect) {
                is Message -> {
                    when (controller.showSnackbar(
                        message = (effect as Message).text,
                        actionText = (effect as Message).labelAction
                    )) {
                        SnackbarResult.Dismissed -> {

                        }

                        SnackbarResult.ActionPerformed -> {
                            (effect as Message).action?.invoke()
                        }
                    }
                }

                is Empty -> {

                }

                is DialogEmpty -> {
                    showsDialog = false
                }

                is DialogWarning -> {
                    showsDialog = true
                }

                is DialogCreate -> {
                    showsDialog = true
                }

                is ModelInfo -> {

                }
            }

        }
    )

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

        if (trail.currentSelected != Environment.getExternalStorageDirectory().asPath()) {
            return@BackHandler viewModel.navigateToParent()
        }

        (context as? Activity)?.finish()
    }
}