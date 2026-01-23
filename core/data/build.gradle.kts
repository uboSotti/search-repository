
plugins {
    id("kurly.android.library")
    id("kurly.android.hilt")
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.kurly.exam.core.data"

    defaultConfig {
        testInstrumentationRunner = "com.kurly.exam.core.data.HiltTestRunner"
    }
}

kotlin {
    compilerOptions {
        freeCompilerArgs.add("-opt-in=kotlinx.serialization.InternalSerializationApi")
    }
}

dependencies {
    // Project Modules
    implementation(project(":core:common"))
    implementation(project(":core:domain"))
    implementation(project(":core:network"))

    // Network
    implementation(libs.retrofit.core)
    implementation(libs.kotlinx.serialization.json)

    // DataStore
    implementation(libs.androidx.datastore.preferences)

    // Paging
    implementation(libs.androidx.paging.runtime)

    // Android Test
    androidTestImplementation(libs.bundles.test.android)
    androidTestImplementation(libs.kotlinx.coroutines.test)
    androidTestImplementation(project(":core:mockserver"))
    androidTestImplementation(libs.hilt.android.testing)
    kspAndroidTest(libs.hilt.compiler)
}
