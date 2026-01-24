plugins {
    alias(libs.plugins.kurly.android.library.compose)
}

android {
    namespace = "com.kurly.exam.core.ui"
}

dependencies {
    implementation(project(":core:model"))

    // UI
    api(libs.coil.compose)
    api(libs.coil.network.okhttp)
}