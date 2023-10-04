import com.andanana.musicplayer.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class AndroidHiltConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("dagger.hilt.android.plugin")
                // KAPT must go last to avoid build warnings.
                // See: https://stackoverflow.com/questions/70550883/warning-the-following-options-were-not-recognized-by-any-processor-dagger-f
                apply("org.jetbrains.kotlin.kapt")
            }

            dependencies {
                "implementation"(libs.findLibrary("com.google.dagger.hilt.android").get())
                "kapt"(libs.findLibrary("com.google.dagger.hilt.compiler").get())
                "kaptAndroidTest"(libs.findLibrary("com.google.dagger.hilt.compiler").get())
            }

        }
    }

}