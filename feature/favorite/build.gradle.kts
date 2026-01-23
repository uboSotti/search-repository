plugins {
    alias(libs.plugins.kurly.android.library.compose)
    alias(libs.plugins.kurly.android.hilt)
}

android {
    namespace = "com.kurly.exam.feature.favorite"
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":core:domain"))
    implementation(project(":core:model"))
    implementation(project(":core:ui"))

    // Test
    testImplementation(libs.bundles.test.unit)
}
