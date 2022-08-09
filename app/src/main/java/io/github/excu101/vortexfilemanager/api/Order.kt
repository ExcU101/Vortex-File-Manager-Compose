package io.github.excu101.vortexfilemanager.api

import io.github.excu101.vortexfilemanager.data.FileModel

sealed class Order(val comparator: Comparator<FileModel>) {

}