plugins {
    kotlin("jvm")
    alias(libs.plugins.ksp)
}

dependencies {
    implementation(libs.javax.inject)

    // Paging
    implementation(libs.androidx.paging.common)

    // Test
    testImplementation(libs.bundles.test.unit)
    testImplementation(libs.kotlinx.coroutines.test)
    testRuntimeOnly(libs.junit.jupiter.engine)
}
