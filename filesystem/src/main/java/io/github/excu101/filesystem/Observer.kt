package io.github.excu101.filesystem

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

interface Observer<T> {

    fun onNext(value: T)

    fun onError(error: Throwable)

    fun onComplete()

}

inline fun <T> observer(
    crossinline onNext: (T) -> Unit,
    crossinline onError: (Throwable) -> Unit,
    crossinline onComplete: () -> Unit,
): Observer<T> {
    return object : Observer<T> {
        override fun onNext(value: T) {
            onNext.invoke(value)
        }

        override fun onError(error: Throwable) {
            onError.invoke(error)
        }

        override fun onComplete() {
            onComplete.invoke()
        }
    }
}

inline fun <T> observer(
    scope: CoroutineScope,
    crossinline onNext: suspend (T) -> Unit,
    crossinline onError: suspend (Throwable) -> Unit,
    crossinline onComplete: suspend () -> Unit,
): Observer<T> {
    return object : Observer<T> {
        override fun onNext(value: T) {
            scope.launch { onNext.invoke(value) }
        }

        override fun onError(error: Throwable) {
            scope.launch { onError.invoke(error) }
        }

        override fun onComplete() {
            scope.launch { onComplete.invoke() }
        }
    }
}