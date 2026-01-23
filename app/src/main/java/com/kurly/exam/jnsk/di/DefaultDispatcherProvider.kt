package com.kurly.exam.jnsk.di

import com.kurly.exam.core.common.dispatcher.DispatcherProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

/**
 * Android 플랫폼에 종속적인 [DispatcherProvider]의 기본 구현체.
 * [Dispatchers]를 사용하여 각 CoroutineDispatcher를 제공합니다.
 */
class DefaultDispatcherProvider : DispatcherProvider {
    /** Main 스레드에서 실행되는 디스패처. UI 작업에 사용됩니다. */
    override val main: CoroutineDispatcher
        get() = Dispatchers.Main

    /** I/O 작업을 위한 디스패처. 네트워크 또는 디스크 작업에 사용됩니다. */
    override val io: CoroutineDispatcher
        get() = Dispatchers.IO

    /** CPU 집약적인 작업을 위한 기본 디스패처. */
    override val default: CoroutineDispatcher
        get() = Dispatchers.Default
}
