import com.andannn.melodify.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class AndroidTestingConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            dependencies {
                "testImplementation"(libs.findLibrary("junit4").get())
                "androidTestImplementation"(libs.findLibrary("androidx.test.ext").get())

                "androidTestImplementation"(libs.findLibrary("androidx.test.runner").get())

                "testImplementation"(libs.findLibrary("kotlinx.coroutines.test").get())
                "androidTestImplementation"(libs.findLibrary("kotlinx.coroutines.test").get())

                "implementation"(libs.findLibrary("kotlinx.coroutines.android").get())
            }
        }
    }

}