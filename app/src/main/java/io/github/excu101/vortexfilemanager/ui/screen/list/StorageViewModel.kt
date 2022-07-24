package io.github.excu101.vortexfilemanager.ui.screen.list

import android.os.Build
import android.os.Environment
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.excu101.filesystem.FileProvider
import io.github.excu101.filesystem.fs.attr.StandardOptions
import io.github.excu101.filesystem.fs.path.Path
import io.github.excu101.filesystem.fs.utils.asPath
import io.github.excu101.filesystem.observer
import io.github.excu101.filesystem.unix.operation.UnixCreateDirectoryOperation
import io.github.excu101.filesystem.unix.operation.UnixCreateFileOperation
import io.github.excu101.filesystem.unix.operation.UnixDeleteOperation
import io.github.excu101.vortexfilemanager.base.utils.ViewModelContainerHandler
import io.github.excu101.vortexfilemanager.base.utils.intent
import io.github.excu101.vortexfilemanager.base.utils.reduce
import io.github.excu101.vortexfilemanager.base.utils.side
import io.github.excu101.vortexfilemanager.data.*
import io.github.excu101.vortexfilemanager.data.intent.Contracts.SideEffect
import io.github.excu101.vortexfilemanager.data.intent.Contracts.State.StorageScreenState
import io.github.excu101.vortexfilemanager.provider.AndroidFileProvider
import io.github.excu101.vortexfilemanager.provider.DataStoreOwner
import io.github.excu101.vortexfilemanager.provider.ErrorHandler
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StorageViewModel @Inject constructor(
    private val handle: SavedStateHandle,
    private val provider: AndroidFileProvider,
    private val owner: DataStoreOwner,
) : ViewModelContainerHandler<StorageScreenState, SideEffect>(StorageScreenState(isLoading = true)),
    ErrorHandler {

    companion object Constants {
        private const val PATH_KEY = "path"
        private const val TRAIL_KEY = "trail"
    }

    private val _currentComparator: MutableStateFlow<Comparator<FileModel>> = MutableStateFlow(
        compareBy<FileModel> { model ->
            model.isDirectory
        }.thenBy { model ->
            model.size.inputMemory
        }.thenBy { model ->
            model.name
        }
    )

    val currentComparator: StateFlow<Comparator<FileModel>>
        get() = _currentComparator.asStateFlow()

    private val _selected = mutableStateListOf<FileModel>()
    val selected: FileModelSet
        get() = fileModelSetOf(_selected)

    val trail: StateFlow<Trail> = handle.getStateFlow(
        key = TRAIL_KEY,
        initialValue = Trail(segments = listOf(), selected = -1)
    )

    val currentPath: StateFlow<FileModel> = handle.getStateFlow(
        key = PATH_KEY,
        initialValue = FileModel(Environment.getExternalStorageDirectory().asPath())
    )

    private val _currentOperation = MutableStateFlow<Operation?>(value = null)
    val currentOperation: StateFlow<Operation?>
        get() = _currentOperation.asStateFlow()

    init {
        launch()
    }

    fun checkPerm() = intent {
        reduce { state.copy(isLoading = true) }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (!provider.checkManagePerm()) {
                reduce { StorageScreenState(requiresSpecialPermission = true) }
            } else {
                navigateTo(currentPath.value)
            }
        } else {
            if (provider.requiresReadWritePerm()) {
                reduce { StorageScreenState(requiresPermission = true) }
            } else {
                navigateTo(currentPath.value)
            }
        }
    }

    infix fun choose(model: FileModelSet) {
        if (selected.containsAll(model)) deselect(model) else select(model)
    }

    infix fun select(
        files: FileModelSet,
    ) = intent {
        files.forEach { model ->
            if (!selected.contains(model)) {
                _selected.add(model)
            }
        }
    }

    fun deselect(
        files: FileModelSet,
    ) = intent {
        _selected.removeAll(files)
    }

    fun deselectLast() = intent {
        deselect(fileModelSetOf(selected.last()))
    }

    fun selectAll() = intent {
        _selected.addAll(state.data)
    }

    fun deselectAll() = intent {
        _selected.clear()
    }

    fun navigateToParent() {
        trail.value.currentSelected.parent?.let { navigateTo(it) }
    }

    infix fun navigateTo(
        model: FileModel,
    ) = intent {
        reduce { state.copy(isLoading = true) }
        handle[TRAIL_KEY] = trail.value.navigateTo(model.path)
        if (model.isDirectory) {
            val list = try {
                fileModelSetOf(
                    models = provider.provide(path = model.path).map(::FileModel)
                        .sortedWith(currentComparator.value)
                )
            } catch (e: Exception) {
                reduce { state.copy(error = e) }
                return@intent
            }

            if (list.isEmpty()) {
                reduce {
                    StorageScreenState(
                        error = Throwable("${model.name} doesn't contain any elements")
                    )
//                    Warning(
//                        icon = Icons.Outlined.Folder,
//                        message = "${model.name} doesn't contain any elements",
//                        actions = listBuilder {
//                            item(
//                                title = "Back to parent",
//                                icon = Icons.Outlined.KeyboardArrowLeft,
//                            )
//                            item(
//                                title = "Add some content",
//                                icon = Icons.Outlined.Add,
//                            )
//                        }
//                    )
                }
            } else {
                reduce {
                    StorageScreenState(data = list)
                }
            }
        } else {
            reduce {
                StorageScreenState(
                    error = Throwable("File opening currently not supported${model.name}")
                )
            }
        }
    }

    fun delete(data: FileModelSet = selected) = intent {
        var progress = 0
        FileProvider.runOperation(
            UnixDeleteOperation(
                data = data.map(FileModel::path),
                observer = observer(
                    scope = viewModelScope,
                    onNext = {
                        progress++
                        _currentOperation.emit(value = Operation(progress = "Deleting... ($progress/${data.size})"))
                    },
                    onComplete = {
                        _currentOperation.emit(value = null)
                    },
                    onError = { error ->
                        _currentOperation.emit(value = Operation(progress = error.message.toString()))
                        delay(2000L)
                        _currentOperation.emit(value = null)
                        removePath(data)
                    }
                )
            )
        )
    }

    fun showDialog(effect: SideEffect) = intent {
        side(effect)
    }

    fun hideDialog() = intent {
        side(SideEffect.DialogEmpty)
    }

    fun launch() = intent {
        checkPerm()
    }

    fun addPath(models: FileModelSet) = intent {
        reduce { state.copy(data = fileModelSetOf(state.data + models)) }
    }

    fun removePath(models: FileModelSet) = intent {
        if (trail.value[models.map(FileModel::path)]) {
            handle[TRAIL_KEY] = trail.value.slice(models)
        }
        reduce { state.copy(data = fileModelSetOf(state.data - models)) }
    }

    override fun onError(error: Throwable) = intent {
        reduce { StorageScreenState(error = error) }
    }

    fun createDirectory(dest: Path) = intent {
        UnixCreateDirectoryOperation(
            path = dest,
            mode = 777,
            observer = observer(
                onNext = {
                    viewModelScope.launch {

                    }
                },
                onError = ::onError,
                onComplete = {
                    addPath(fileModelSetOf(FileModel(dest)))
                }
            )
        ).perform()
    }

    fun createFile(dest: Path) = intent {
        UnixCreateFileOperation(
            path = dest,
            flags = setOf(StandardOptions.WRITE, StandardOptions.READ, StandardOptions.APPEND),
            mode = 777,
            observer = observer(
                onNext = {

                },
                onError = ::onError,
                onComplete = {
                    addPath(fileModelSetOf(FileModel(dest)))
                }
            )
        ).perform()
    }
}