plugins {
    `kotlin-dsl`
}

group = "com.kurly.exam.buildlogic"

dependencies {
    compileOnly(libs.android.tools.build.gradle.plugin)
    compileOnly(libs.kotlin.gradle.plugin)
}

gradlePlugin {
    plugins {
        register("kurlyJvmLibrary") {
            id = "kurly.jvm.library"
            implementationClass = "KurlyJvmLibraryConventionPlugin"
        }
        register("kurlyAndroidLibrary") {
            id = "kurly.android.library"
            implementationClass = "KurlyAndroidLibraryConventionPlugin"
        }
        register("kurlyAndroidHilt") {
            id = "kurly.android.hilt"
            implementationClass = "AndroidHiltConventionPlugin"
        }
        register("kurlyAndroidApplication") {
            id = "kurly.android.application"
            implementationClass = "AndroidApplicationConventionPlugin"
        }
        register("kurlyAndroidLibraryCompose") {
            id = "kurly.android.library.compose"
            implementationClass = "AndroidLibraryComposeConventionPlugin"
        }
        register("kurlyAndroidApplicationCompose") {
            id = "kurly.android.application.compose"
            implementationClass = "AndroidApplicationComposeConventionPlugin"
        }
    }
}
