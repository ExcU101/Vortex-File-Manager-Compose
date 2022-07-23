package io.github.excu101.pluginsystem.common

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import io.github.excu101.filesystem.FileProvider
import io.github.excu101.filesystem.unix.UnixFileSystem
import io.github.excu101.filesystem.unix.UnixFileSystemProvider
import io.github.excu101.filesystem.unix.operation.UnixCopyOperation
import io.github.excu101.filesystem.unix.operation.UnixDeleteOperation
import io.github.excu101.filesystem.unix.operation.UnixRenameOperation
import io.github.excu101.pluginsystem.dsl.registers
import io.github.excu101.pluginsystem.model.Plugin

class DefaultOperationsPlugin : Plugin {

    override val attributes: Plugin.Attributes = object : Plugin.Attributes {

        override val name: String
            get() = "Default operations plugin"

        override val version: String
            get() = "1.0.0"

        override val packageName: String
            get() = "io.github.excu101.plugins"
    }

    override fun activate() = registers {
        FileProvider.installFileSystem(system = UnixFileSystem(UnixFileSystemProvider()))
        registerGroup {
            name = "Default operations"
            icon = Icons.Outlined.DisabledByDefault

            registerAction {
                title = "Delete"
                icon = Icons.Outlined.Delete
                operation = UnixDeleteOperation::class
            }

            registerAction {
                title = "Copy"
                icon = Icons.Outlined.ContentCopy
                operation = UnixCopyOperation::class
            }

            registerAction {
                title = "Move"
                icon = Icons.Outlined.DriveFileMove
                operation = UnixRenameOperation::class
            }

            registerAction {
                title = "Rename"
                icon = Icons.Outlined.DriveFileRenameOutline
                operation = UnixRenameOperation::class
            }
        }
    }
}