package io.github.excu101.vortexfilemanager.ui.screen.list

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.app.Activity
import android.os.Environment
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.google.accompanist.systemuicontroller.SystemUiController
import io.github.excu101.filesystem.fs.utils.asPath
import io.github.excu101.pluginsystem.model.Action
import io.github.excu101.pluginsystem.model.GroupAction
import io.github.excu101.ui.component.layout.ErrorMessage
import io.github.excu101.ui.component.layout.ErrorTextStyle
import io.github.excu101.ui.component.layout.ProgressBarView
import io.github.excu101.ui.component.layout.SelectableSectionList
import io.github.excu101.vortexfilemanager.base.utils.collectAsEffect
import io.github.excu101.vortexfilemanager.base.utils.collectAsEffectWithLifecycle
import io.github.excu101.vortexfilemanager.base.utils.collectAsStateWithLifecycle
import io.github.excu101.vortexfilemanager.data.intent.SideEffect.Empty
import io.github.excu101.vortexfilemanager.data.intent.SideEffect.Message
import io.github.excu101.vortexfilemanager.data.intent.StorageDialogState.*
import io.github.excu101.vortexfilemanager.data.toInfo
import io.github.excu101.vortexfilemanager.provider.ScopedStorageContract
import io.github.excu101.vortexfilemanager.ui.MainScreenController
import io.github.excu101.vortexfilemanager.ui.screen.list.view.*
import io.github.excu101.vortexfilemanager.ui.theme.Theme
import io.github.excu101.vortexfilemanager.ui.theme.key.layoutProgressTitleTextColorKey
import io.github.excu101.vortexfilemanager.ui.util.toggle
import io.github.excu101.vortexfilemanager.ui.view.action.*
import io.github.excu101.vortexfilemanager.ui.view.icon.MenuIconState
import io.github.excu101.vortexfilemanager.ui.view.trail.TrailRow
import io.github.excu101.vortexfilemanager.util.drawerColors
import io.github.excu101.vortexfilemanager.util.progressBarColors
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class, ExperimentalLifecycleComposeApi::class)
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
        onResult = viewModel::onPermissionResult
    )

    val contractCall: (Action) -> Unit = remember(viewModel) {
        {
            contract.launch(
                arrayOf(
                    READ_EXTERNAL_STORAGE,
                    WRITE_EXTERNAL_STORAGE
                )
            )
        }
    }

    val contractSpecial = rememberLauncherForActivityResult(
        contract = ScopedStorageContract,
        onResult = viewModel::onSpecialPermissionResult
    )

    val contractSpecialCall: (Action) -> Unit = remember(viewModel) {
        {
            contractSpecial.launch()
        }
    }

    val effect by viewModel.collectAsEffectWithLifecycle(initial = Empty)
    val state by viewModel.collectAsStateWithLifecycle()
    val trail by viewModel.trail.collectAsStateWithLifecycle()
    val dialog by viewModel.dialog.collectAsStateWithLifecycle()
    val selected = remember { viewModel.selected }

    val mode by remember { viewModel.mode }

    var isBarHide by remember { controller.bar.isHide }

    val bottom = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)

    var sections by remember {
        mutableStateOf(listOf<GroupAction>())
    }

    ModalBottomSheetLayout(
        sheetState = bottom,
        sheetContent = {
            SelectableSectionList(
                colors = drawerColors(),
                sections = sections,
                onActionClick = controller::notifyActionListeners,
                selectedActions = listOf(),
                onEmpty = {
                    Text(modifier = Modifier.padding(16.dp), text = "Here's no content")
                }
            )
        },
    ) {
        StorageScaffold(
            trail = {
                val segments = trail.segments
                val selectedSegment = trail.selected
                val info = trail.currentSelected.toInfo()

                TrailRow(
                    segments = segments,
                    currentSelected = selectedSegment,
                    onTrailClick = viewModel::navigateTo
                )
            },
        ) {
            val scroller = rememberLazyListState()
            val isScrolling by remember {
                derivedStateOf(scroller::isScrollInProgress)
            }
            isBarHide = isScrolling

            StorageList(
                modifier = Modifier.padding(it),
                data = state.data,
                selected = selected,
                state = scroller,
                onSelect = viewModel::select,
                onItemClick = viewModel::navigateTo,
                mode = mode
            )

            if (state.error != null) {
                ErrorMessage(
                    modifier = Modifier.fillMaxSize(),
                    message = state.error!!.message.toString(),
                    style = ErrorTextStyle.copy(color = Theme[layoutProgressTitleTextColorKey])
                )
            }

            if (state.requiresPermission) {
                PermWarning(
                    modifier = Modifier.fillMaxSize(),
                    onActionClick = contractCall,
                )
            }

            if (state.requiresSpecialPermission) {
                SpecialPermWarning(
                    modifier = Modifier.fillMaxSize(),
                    onActionClick = contractSpecialCall
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
                onDismissRequest = viewModel::hideDialog,
                onCancelRequest = viewModel::hideDialog,
                onConfirmRequest = viewModel::onConfirmCreate
            )
        }

        is StorageWarningDialog -> {
            FileListWarningDialog(
                message = it.message,
                onDismissRequest = viewModel::hideDialog,
                onCancelRequest = viewModel::hideDialog,
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
            if (!controller.bar.actions.value.contains(SortAction)) {
                controller.bar.actions.value += SortAction
            }
            if (!controller.bar.actions.value.contains(MoreAction)) {
                controller.bar.actions.value += MoreAction
            }
        }
    )

    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(
        key1 = lifecycleOwner,
        effect = {
            controller.subscribeOnAction(index = 1) {
                when (it.title) {

                    "Select all" -> viewModel.selectAll()

                    "Deselect all" -> viewModel.deselectAll()

                    "Back to parent" -> viewModel.navigateToParent()

                    "Open folder" -> {
                        viewModel.openFolder()
                        scope.launch { bottom.hide() }
                    }

                    "More" -> {
                        sections = Defaults(selected = selected.toTypedArray()).asGroup()
                        bottom.toggle(scope = scope)
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