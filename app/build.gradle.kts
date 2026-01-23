plugins {
    alias(libs.plugins.kurly.android.application)
    alias(libs.plugins.kurly.android.application.compose)

    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.kurly.exam.jnsk"
    defaultConfig {
        applicationId = "com.kurly.exam.jnsk"

        versionCode = 1
        versionName = "1.0"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":core:data"))
    implementation(project(":core:domain"))
    implementation(project(":core:ui"))
    implementation(project(":feature:favorite"))
    implementation(project(":feature:main"))

    // UI
    implementation(libs.navigation3.ui)
    implementation(libs.androidx.lifecycle.viewmodel.navigation3)

    // Test
    testImplementation(libs.junit)
    androidTestImplementation(libs.bundles.test.android)
}
