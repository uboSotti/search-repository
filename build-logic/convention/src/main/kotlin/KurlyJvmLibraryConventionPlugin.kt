import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType

class KurlyJvmLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("org.jetbrains.kotlin.jvm")

            val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")
            dependencies {
                "testImplementation"(libs.findBundle("test-unit").get())
                "testImplementation"(libs.findLibrary("kotlinx.coroutines.test").get())
                "testRuntimeOnly"(libs.findLibrary("junit.jupiter.engine").get())
            }
        }
    }
}
