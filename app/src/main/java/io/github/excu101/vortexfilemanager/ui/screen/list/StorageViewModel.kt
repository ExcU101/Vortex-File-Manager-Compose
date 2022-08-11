package io.github.excu101.vortexfilemanager.ui.screen.list

import android.os.Build
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.excu101.filesystem.FileProvider
import io.github.excu101.filesystem.fs.attr.StandardOptions
import io.github.excu101.filesystem.fs.path.Path
import io.github.excu101.filesystem.fs.utils.FileOperationObserver
import io.github.excu101.filesystem.fs.utils.parsePath
import io.github.excu101.filesystem.unix.operation.UnixCreateDirectoryOperation
import io.github.excu101.filesystem.unix.operation.UnixCreateFileOperation
import io.github.excu101.filesystem.unix.operation.UnixDeleteOperation
import io.github.excu101.vortexfilemanager.base.Container
import io.github.excu101.vortexfilemanager.base.ContainerHandler
import io.github.excu101.vortexfilemanager.base.utils.*
import io.github.excu101.vortexfilemanager.data.FileModel
import io.github.excu101.vortexfilemanager.data.FileModel.Companion.SDModel
import io.github.excu101.vortexfilemanager.data.MutableFileModelSet
import io.github.excu101.vortexfilemanager.data.Trail
import io.github.excu101.vortexfilemanager.data.fromPathToModels
import io.github.excu101.vortexfilemanager.data.intent.SideEffect
import io.github.excu101.vortexfilemanager.data.intent.StorageDialogState
import io.github.excu101.vortexfilemanager.data.intent.StorageDialogState.StorageCreateDialog
import io.github.excu101.vortexfilemanager.data.intent.StorageDialogState.StorageEmptyDialog
import io.github.excu101.vortexfilemanager.data.intent.StorageState
import io.github.excu101.vortexfilemanager.provider.AndroidFileProvider
import io.github.excu101.vortexfilemanager.provider.DataStoreOwner
import io.github.excu101.vortexfilemanager.provider.ErrorHandler
import io.github.excu101.vortexfilemanager.provider.FileModelSorter
import io.github.excu101.vortexfilemanager.provider.FileModelSorter.*
import io.github.excu101.vortexfilemanager.ui.screen.list.view.StorageListViewMode
import io.github.excu101.vortexfilemanager.util.applier
import io.github.excu101.vortexfilemanager.util.emit
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class StorageViewModel @Inject constructor(
    private val handle: SavedStateHandle,
    private val provider: AndroidFileProvider,
) : ViewModel(), ContainerHandler<StorageState, SideEffect>, ErrorHandler {

    companion object Constants {
        private const val TRAIL_KEY = "trail"
    }

    override val container: Container<StorageState, SideEffect> =
        container(StorageState(isLoading = false, loadingMessage = "Launching..."))


    private val _selected = mutableStateListOf<FileModel>()
    val selected: List<FileModel>
        get() = _selected

    val isAllSelected: Boolean
        get() = selected === state.data

    var mode = mutableStateOf(StorageListViewMode.NORMAL)

    var sort = Sort.NAME
    var order = Order.A_Z
    var filter = Filter.EMPTY

    private val _dialog: MutableStateFlow<StorageDialogState> =
        MutableStateFlow(StorageEmptyDialog)
    val dialog: StateFlow<StorageDialogState>
        get() = _dialog.asStateFlow()

    val trail: StateFlow<Trail>
        get() = handle.getStateFlow(
            TRAIL_KEY,
            Trail(segments = fromPathToModels(SDModel.path))
        )

    val currentModel: FileModel
        get() = trail.value.currentSelected

    init {
        launch()
    }

    fun checkPerm() = intent {
        reduce { state.copy(loadingMessage = "Checking...", isLoading = true) }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (!provider.checkManagePerm()) {
                reduce { StorageState(requiresSpecialPermission = true) }
            } else {
                navigateTo()
            }
        } else {
            if (provider.requiresReadWritePerm()) {
                reduce { StorageState(requiresPermission = true) }
            } else {
                navigateTo()
            }
        }
    }

    fun navigateToParent() {
        trail.value.currentSelected.parent?.let { navigateTo(it) }
    }

    fun select(models: List<FileModel>, isSelected: Boolean = _selected.containsAll(models)) {
        if (isSelected) {
            _selected.removeAll(models)
        } else {
            _selected.addAll(models)
        }
    }

    fun select(model: FileModel, isSelected: Boolean = _selected.contains(model)) {
        select(listOf(model), isSelected)
    }

    fun selectAll() = intent {
        select(models = state.data, isSelected = false)
    }

    fun deselectAll() = intent {
        select(models = state.data, isSelected = true)
    }

    fun navigateTo(
        model: FileModel = trail.value.currentSelected,
    ) = intent {
        reduce { state.copy(isLoading = true, loadingMessage = "Navigating...") }
        handle[TRAIL_KEY] = trail.value.navigateTo(model.path)
        if (model.isDirectory) {
            val list = try {
                FileModelSorter(provider.provide(path = model.path))
                    .with(sort)
                    .with(order)
                    .with(filter)
                    .output()
            } catch (error: Exception) {
                onError(error = error)
                return@intent
            }

            if (list.isEmpty()) {
                onError(error = Throwable("${model.name} doesn't contain any elements"))
            } else {
                reduce {
                    StorageState(data = list)
                }
            }
        } else {
            onError(Throwable("File opening currently not supported${model.name}"))
        }
    }

    fun createDialog() = intent {
        _dialog.emit(StorageCreateDialog)
    }

    fun hideDialog() = intent {
        _dialog.emit(StorageEmptyDialog)
    }

    fun launch() = intent {
        checkPerm()
    }

    fun openFolder() {
        val model = selected.first()
        navigateTo(model = model)
        select(model, isSelected = false)
    }

    fun addPath(models: List<FileModel>) = intent {
        reduce { state.copy(data = state.data + models) }
    }

    fun removePath(models: List<FileModel>) = intent {
        select(models, isSelected = false)
        handle[TRAIL_KEY] = trail.value.navigateUp()

        reduce { state.copy(data = state.data - models.toSet()) }
    }

    override fun onError(error: Throwable) = intent {
        reduce { StorageState(isLoading = false, error = error) }
    }

    fun sort(
        sort: Sort = this.sort,
        order: Order = this.order,
        filter: Filter = this.filter,
    ) {

    }

    fun onConfirmCreate(isDirectory: Boolean, dest: String) = if (isDirectory) {
        createDirectory(dest = parsePath(dest))
    } else {
        createFile(dest = parsePath(dest))
    }

    fun delete() = intent {
        val data: List<FileModel> =
            if (selected.isEmpty()) listOf(currentModel) else selected.toList()

        data.forEach {
            logger(it.name)
        }

        FileProvider.runOperation(
            operation = UnixDeleteOperation(data = data.map(FileModel::path)),
            observers = listOf(
                FileOperationObserver(
                    scope = viewModelScope,
                    completion = {
                        removePath(data)
                    },
                    error = { error ->
                        onError(error = error)
                    },
                    action = { path ->
                        side(SideEffect.Message(text = "Deleting... ($path"))
                    },
                )
            )
        )
    }

    private fun createDirectory(dest: Path) = intent {
        hideDialog()
        val src = if (selected.isEmpty()) currentModel.path else selected.first().path
        val cDest = src.resolve(dest)
        FileProvider.runOperation(
            operation = UnixCreateDirectoryOperation(path = cDest, mode = 777),
            observers = listOf(
                FileOperationObserver(
                    scope = viewModelScope,
                    action = { value ->
                        side(SideEffect.Message(text = "Creating ${value.getName()}"))
                    },
                    error = { error -> onError(error = error) },
                    completion = {
                        addPath(listOf(FileModel(cDest)))
                    }
                )
            )
        )
    }

    private fun createFile(dest: Path) = intent {
        hideDialog()

        val src = if (selected.isEmpty()) currentModel.path else selected.first().path
        val cDest = src.resolve(dest)

        FileProvider.runOperation(
            operation = UnixCreateFileOperation(
                path = dest,
                flags = setOf(StandardOptions.WRITE, StandardOptions.READ, StandardOptions.APPEND),
                mode = 777
            ),
            observers = listOf(
                FileOperationObserver(
                    scope = viewModelScope,
                    action = { path ->
                        side(SideEffect.Message(text = "Creating ${dest.getName()}"))
                    },
                    error = { error -> onError(error) },
                    completion = {
                        addPath(listOf(FileModel(cDest)))
                    }
                )
            )
        )
    }

    fun onPermissionResult(perms: Map<String, Boolean>) {
        perms.forEach { (_, granted) ->
            if (!granted) {
                launch()
            } else {
                navigateTo()
            }
        }
    }

    fun onSpecialPermissionResult(result: Boolean) {
        if (!result) {
            launch()
        } else {
            navigateTo()
        }
    }
}