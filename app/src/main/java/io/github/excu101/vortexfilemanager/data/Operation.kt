package io.github.excu101.vortexfilemanager.data

import android.os.Parcelable
import androidx.compose.runtime.Immutable
import kotlinx.parcelize.Parcelize

@Immutable
@Parcelize
data class Operation(
    val progress: String,
) : Parcelable

