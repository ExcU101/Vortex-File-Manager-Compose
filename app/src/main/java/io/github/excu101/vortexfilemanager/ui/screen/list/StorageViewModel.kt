package io.github.excu101.vortexfilemanager.ui.screen.list

import android.os.Build
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.excu101.filesystem.FileProvider
import io.github.excu101.filesystem.fs.attr.StandardOptions
import io.github.excu101.filesystem.fs.path.Path
import io.github.excu101.filesystem.fs.utils.FileOperationObserver
import io.github.excu101.filesystem.unix.operation.UnixCreateDirectoryOperation
import io.github.excu101.filesystem.unix.operation.UnixCreateFileOperation
import io.github.excu101.filesystem.unix.operation.UnixDeleteOperation
import io.github.excu101.vortexfilemanager.base.Container
import io.github.excu101.vortexfilemanager.base.ContainerHandler
import io.github.excu101.vortexfilemanager.base.utils.container
import io.github.excu101.vortexfilemanager.base.utils.intent
import io.github.excu101.vortexfilemanager.base.utils.reduce
import io.github.excu101.vortexfilemanager.base.utils.side
import io.github.excu101.vortexfilemanager.data.FileModel
import io.github.excu101.vortexfilemanager.data.FileModel.Companion.SDModel
import io.github.excu101.vortexfilemanager.data.FileModelSet
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
import io.github.excu101.vortexfilemanager.util.applier
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class StorageViewModel @Inject constructor(
    private val handle: SavedStateHandle,
    private val provider: AndroidFileProvider,
    private val owner: DataStoreOwner,
) : ViewModel(), ContainerHandler<StorageState, SideEffect>, ErrorHandler {

    companion object Constants {
        private const val TRAIL_KEY = "trail"
    }

    override val container: Container<StorageState, SideEffect> =
        container(StorageState(isLoading = false, loadingMessage = "Launching..."))

    private val _data = mutableStateListOf<FileModel>()
    val data: List<FileModel>
        get() = _data

    private val _selected = MutableStateFlow(FileModelSet())
    val selected: StateFlow<FileModelSet>
        get() = _selected.asStateFlow()

    val isAllSelected: Boolean
        get() = selected === data

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

    infix fun choose(model: List<FileModel>) {
        if (selected.value.containsAll(model)) deselect(model) else select(model)
    }

    fun select(
        files: List<FileModel>,
    ) = intent {
        _selected.applier { addAll(files) }
    }

    fun deselect(
        files: List<FileModel>,
    ) = intent {
        _selected.applier {
            removeAll(files.toSet())
        }
    }

    fun deselectLast() = intent {
        _selected.applier {
            remove(last())
        }
    }

    fun selectAll() = intent {
        select(state.data)
    }

    fun deselectAll() = intent {
        _selected.emit(FileModelSet())
    }

    fun navigateToParent() {
        trail.value.currentSelected.parent?.let { navigateTo(it) }
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

    fun delete() = intent {
        val data: List<FileModel> =
            if (selected.value.isEmpty()) listOf(currentModel) else selected.value.toList()
        var progress = 0
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
                    action = {
                        progress++
                        side(SideEffect.Message(text = "Deleting... ($progress/${data.size})"))
                    },
                )
            )
        )
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
        val model = selected.value.first()
        navigateTo(model = model)
        deselect(listOf(model))
    }

    fun addPath(models: List<FileModel>) = intent {
//        reduce {
//            (
//                data = (state as StorageData).data + models
//            )
//        }
//        reduce { StorageData(data = (state as StorageData).data + models) }
    }

    fun removePath(models: List<FileModel>) = intent {
        if (trail.value[models.map(FileModel::path)]) {
            handle[TRAIL_KEY] = trail.value.slice(models)
        }
//        reduce { StorageData(data = (state as StorageData).data - models.toSet()) }
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

    fun createDirectory(dest: Path) = intent {
        hideDialog()
        val src = if (selected.value.isEmpty()) currentModel.path else selected.value.first().path
        FileProvider.runOperation(
            operation = UnixCreateDirectoryOperation(path = src.resolve(dest), mode = 777),
            observers = listOf(
                FileOperationObserver(
                    scope = viewModelScope,
                    action = { value ->
                        side(SideEffect.Message(text = "Creating ${value.getName()}"))
                    },
                    error = { error -> onError(error = error) },
                    completion = {
                        addPath(listOf(FileModel(dest)))
                    }
                )
            )
        )
    }

    fun createFile(dest: Path) = intent {
        hideDialog()
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
                        addPath(listOf(FileModel(dest)))
                    }
                )
            )
        )
    }
}