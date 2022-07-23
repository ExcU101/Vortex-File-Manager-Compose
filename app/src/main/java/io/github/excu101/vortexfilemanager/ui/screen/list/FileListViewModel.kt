package io.github.excu101.vortexfilemanager.ui.screen.list

import android.os.Build
import android.os.Environment
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Folder
import androidx.compose.material.icons.outlined.KeyboardArrowLeft
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
import io.github.excu101.vortexfilemanager.data.intent.Contracts.State
import io.github.excu101.vortexfilemanager.data.intent.Contracts.State.*
import io.github.excu101.vortexfilemanager.data.intent.loading
import io.github.excu101.vortexfilemanager.provider.AndroidFileProvider
import io.github.excu101.vortexfilemanager.provider.DataStoreOwner
import io.github.excu101.vortexfilemanager.provider.ErrorHandler
import io.github.excu101.vortexfilemanager.util.item
import io.github.excu101.vortexfilemanager.util.listBuilder
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FileListViewModel @Inject constructor(
    private val handle: SavedStateHandle,
    private val provider: AndroidFileProvider,
    private val owner: DataStoreOwner,
) : ViewModelContainerHandler<State, SideEffect>(Loading()), ErrorHandler {

    companion object Constants {
        private const val PATH_KEY = "path"
        private const val TRAIL_KEY = "trail"
    }

    private val _selectedFiles: MutableStateFlow<FileModelSet> =
        MutableStateFlow(fileModelSetOf())

    val selectedFiles: StateFlow<FileModelSet>
        get() = _selectedFiles.asStateFlow()

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
        reduce { Loading(text = "Checking permissions...") }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (!provider.checkManagePerm()) {
                reduce { RequiresFullStoragePerm }
            } else {
                navigateTo(currentPath.value)
            }
        } else {
            if (provider.requiresReadWritePerm()) {
                reduce { RequiresPerm }
            } else {
                navigateTo(currentPath.value)
            }
        }
    }

    private var lastScrollIndex = 0

    private val _scrollUp = MutableStateFlow(false)
    val scrollUp: StateFlow<Boolean>
        get() = _scrollUp.asStateFlow()

    fun updateScrollPosition(newScrollIndex: Int) {
        if (newScrollIndex == lastScrollIndex) return

        _scrollUp.value = newScrollIndex > lastScrollIndex
        lastScrollIndex = newScrollIndex
    }

    infix fun choose(model: FileModel) {
        if (_selectedFiles.value.contains(model)) deselect(model) else select(model)
    }

    infix fun select(
        model: FileModel,
    ) = intent {
        val set = FileModelSet().apply {
            addAll(selectedFiles.value)
            add(model)
        }
        _selectedFiles.emit(set)
    }

    fun deselect(
        model: FileModel,
    ) = intent {
        val set = FileModelSet().apply {
            addAll(selectedFiles.value)
            remove(model)
        }

        _selectedFiles.emit(set)
    }

    fun deselectLast() {
        deselect(_selectedFiles.value.last())
    }

    fun selectAll() = intent {
        _selectedFiles.emit((state as FileList).data)
    }

    fun deselectAll() = intent {
        _selectedFiles.emit(fileModelSetOf())
    }

    fun navigateToParent() {
        trail.value.currentSelected.parent?.let { navigateTo(FileModel(it)) }
    }

    infix fun navigateTo(
        model: FileModel,
    ) = intent {
        loading(text = "Navigating...")
        handle[TRAIL_KEY] = trail.value.navigateTo(model.path)
        if (model.isDirectory) {
            val list = try {
                fileModelSetOf(models = provider.provide(path = model.path).map(::FileModel))
            } catch (e: Exception) {
                reduce { CriticalError(e) }
                return@intent
            }

            if (list.isEmpty()) {
                reduce {
                    Warning(
                        icon = Icons.Outlined.Folder,
                        message = "${model.name} doesn't contain any elements",
                        actions = listBuilder {
                            item(
                                title = "Back to parent",
                                icon = Icons.Outlined.KeyboardArrowLeft,
                            )
                            item(
                                title = "Add some content",
                                icon = Icons.Outlined.Add,
                            )
                        }
                    )
                }
            } else {
                reduce {
                    FileList(
                        data = list,
                        actions = listOf()
                    )
                }
            }
        } else {
            reduce {
                Warning(
                    message = "Operation \"file open\" currently not supported",
                    actions = listBuilder {
                        item(
                            title = "Back to parent",
                            icon = Icons.Outlined.KeyboardArrowLeft,
                        )
                    }
                )
            }
        }
    }

    fun delete(data: FileModelSet = selectedFiles.value) = intent {
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
        if (state !is FileList) {
            reduce {
                FileList(
                    data = models
                )
            }
        } else {
            reduce {
                (state as FileList).copy(
                    data = state.data.apply {
                        addAll(models)
                    }
                )
            }
        }
    }

    fun removePath(models: FileModelSet) = intent {
        if (trail.value[models.map(FileModel::path)]) {
            handle[TRAIL_KEY] = trail.value.slice(models.map(FileModel::path))
        }
        if (state is FileList) {
            reduce {
                (state as FileList).copy(
                    data = state.data.apply {
                        removeAll(models)
                    }
                )
            }
        }
    }

    override fun onError(error: Throwable) = intent {
        reduce { CriticalError(error) }
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