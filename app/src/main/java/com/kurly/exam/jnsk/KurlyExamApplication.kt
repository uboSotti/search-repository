package com.kurly.exam.jnsk

import android.app.Application
import android.content.Context
import coil3.ImageLoader
import coil3.SingletonImageLoader
import coil3.request.crossfade
import dagger.hilt.android.HiltAndroidApp

/**
 * KurlyExam 애플리케이션 클래스.
 * Hilt 와 Coil3 이미지 로더를 초기화합니다.
 */
@HiltAndroidApp
class KurlyExamApplication : Application(), SingletonImageLoader.Factory {
    /**
     * Coil3의 ImageLoader 인스턴스를 생성합니다.
     * crossfade 효과가 적용된 이미지 로더를 반환합니다.
     *
     * @param context 애플리케이션 컨텍스트
     * @return crossfade 효과가 활성화된 [ImageLoader] 인스턴스
     */
    override fun newImageLoader(context: Context): ImageLoader {
        return ImageLoader.Builder(context)
            .crossfade(true)
            .build()
    }
}
