plugins {
    id("kurly.android.application")
    id("kurly.android.application.compose")
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
    // Project Modules (가나다순)
    implementation(project(":core:common"))
    implementation(project(":core:data"))
    implementation(project(":core:domain"))
    implementation(project(":core:ui"))
    implementation(project(":feature:main"))

    // UI
    implementation(libs.coil.compose)
    implementation(libs.coil.network.okhttp)

    // Test
    testImplementation(libs.junit)
    androidTestImplementation(libs.bundles.test.android)
}
