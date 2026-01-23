plugins {
    alias(libs.plugins.kurly.android.library)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.kurly.exam.core.model"
}

dependencies {
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.kotlinx.collections.immutable)
}
