package io.github.excu101.vortexfilemanager.api.utils

import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

@OptIn(ExperimentalContracts::class)
fun Any?.isNull(): Boolean {
    contract {
        returns(false) implies (this@isNull != null)
    }
    return this == null
}

@OptIn(ExperimentalContracts::class)
fun Any?.isNotNull(): Boolean {
    contract {
        returns(false) implies (this@isNotNull == null)
    }
    return !isNull()
}

@OptIn(ExperimentalContracts::class)
fun Any?.isString(): Boolean {
    contract {
        returns(false) implies (this@isString !is String)
    }
    return this is String
}