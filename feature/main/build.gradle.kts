
plugins {
    id("kurly.android.library.compose")
    id("kurly.android.hilt")
}

android {
    namespace = "com.kurly.exam.feature.main"
}

dependencies {
    // Project Modules (가나다순)
    implementation(project(":core:common"))
    implementation(project(":core:domain"))
    implementation(project(":core:ui"))

    // Coroutines
    implementation(libs.kotlinx.collections.immutable)

    // UI
    implementation(libs.coil.compose)
    implementation(libs.coil.network.okhttp)

    // Paging
    implementation(libs.androidx.paging.compose)

    // Android Test (Compose는 Convention에 포함됨)
    androidTestImplementation(libs.bundles.test.android)
}
