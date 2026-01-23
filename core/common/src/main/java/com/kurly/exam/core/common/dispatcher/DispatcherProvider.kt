package com.kurly.exam.core.common.dispatcher

import kotlinx.coroutines.CoroutineDispatcher

/**
 * 코루틴 디스패처를 제공하는 인터페이스.
 * 각 디스패처는 특정 유형의 작업을 처리하는 데 사용됩니다.
 */
interface DispatcherProvider {
    /** UI와 상호작용하는 작업을 위한 메인 디스패처. */
    val main: CoroutineDispatcher
    /** 디스크 또는 네트워크 I/O 작업을 위한 디스패처. */
    val io: CoroutineDispatcher
    /** CPU를 많이 사용하는 작업을 위한 기본 디스패처. */
    val default: CoroutineDispatcher
}
