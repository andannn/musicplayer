import com.andanana.melodify.util.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class AndroidHiltConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("dagger.hilt.android.plugin")
                apply("com.google.devtools.ksp")
            }

            dependencies {
                "implementation"(libs.findLibrary("com.google.dagger.hilt.android").get())
                "ksp"(libs.findLibrary("com.google.dagger.hilt.compiler").get())
                "kspAndroidTest"(libs.findLibrary("com.google.dagger.hilt.compiler").get())
            }
        }
    }
}
