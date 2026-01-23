plugins {
    alias(libs.plugins.kurly.android.library)
}

android {
    namespace = "com.kurly.exam.core.domain"
}

dependencies {
    implementation(project(":core:model"))

    implementation(libs.javax.inject)
    implementation(libs.androidx.paging.common)
}
