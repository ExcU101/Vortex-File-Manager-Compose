package io.github.excu101.vortexfilemanager.provider

import io.github.excu101.vortexfilemanager.data.FileModel

class FileModelSorter(private val input: Collection<FileModel>) {

    private var sort = Sort.NAME
    private var order = Order.A_Z
    private var filter = Filter.EMPTY

    fun with(sort: Sort): FileModelSorter {
        this.sort = sort
        return this
    }

    fun with(order: Order): FileModelSorter {
        this.order = order
        return this
    }

    fun with(filter: Filter): FileModelSorter {
        this.filter = filter
        return this
    }

    fun output(): List<FileModel> {
        return input.sortedWith(
            if (order == Order.Z_A) compareByDescending {
                when (sort) {
                    Sort.NAME -> {
                        it.name
                    }
                    Sort.LMT -> {
                        it.attrs.lastModifiedTime.toSeconds()
                    }
                    Sort.CT -> {
                        it.attrs.creationTime.toSeconds()
                    }
                    Sort.LAT -> it.attrs.lastAccessTime.toSeconds()
                    Sort.SIZE -> it.size.inputMemory
                }
            } else compareBy {
                when (sort) {
                    Sort.NAME -> {
                        it.name
                    }
                    Sort.LMT -> {
                        it.attrs.lastModifiedTime.toSeconds()
                    }
                    Sort.CT -> {
                        it.attrs.creationTime.toSeconds()
                    }
                    Sort.LAT -> it.attrs.lastAccessTime.toSeconds()
                    Sort.SIZE -> it.size.inputMemory
                }
            }
        ).filter {
            when (filter) {
                Filter.ONLY_FILES -> !it.isDirectory
                Filter.ONLY_FOLDERS -> it.isDirectory
                Filter.EMPTY -> true
            }
        }
    }

    enum class Sort {
        NAME,
        LMT,
        CT,
        LAT,
        SIZE
    }

    enum class Order {
        A_Z,
        Z_A
    }

    enum class Filter {
        EMPTY,
        ONLY_FILES,
        ONLY_FOLDERS
    }
}