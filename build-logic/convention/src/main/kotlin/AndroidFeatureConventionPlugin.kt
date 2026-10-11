import com.danyastry.encryption.buildlogic.library
import com.danyastry.encryption.buildlogic.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class AndroidFeatureConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        pluginManager.apply("encryption.android.library.compose")

        dependencies {
            add("implementation", project(":core:crypto-api"))
            add("implementation", project(":core:ui"))
            add("implementation", libs.library("androidx-material3"))
            add("testImplementation", project(":core:testing"))
        }
    }
}
