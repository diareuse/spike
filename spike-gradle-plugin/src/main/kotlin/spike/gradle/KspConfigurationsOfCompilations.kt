package spike.gradle

import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.plugin.KotlinCompilation
import org.jetbrains.kotlin.gradle.plugin.KotlinPlatformType
import org.jetbrains.kotlin.gradle.plugin.KotlinTarget
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinAndroidTarget
import spike.Include

@Include
internal class KspConfigurationsOfCompilations(private val compilations: KotlinCompilations) : KspConfigurations {
    override fun each(project: Project, action: (String) -> Unit) {
        val androidTargets = mutableSetOf<String>()
        compilations.each(project) { compilation ->
            val target = compilation.target
            when {
                target.platformType == KotlinPlatformType.common -> Unit
                target.usesAndroidSourceSets() -> {
                    if (androidTargets.add(target.name)) eachAndroidConfiguration(project, target, action)
                }
                else -> eachConfiguration(project, nameOf(compilation), action)
            }
        }
    }

    private fun KotlinTarget.usesAndroidSourceSets() =
        platformType == KotlinPlatformType.androidJvm && this is KotlinAndroidTarget

    private fun nameOf(compilation: KotlinCompilation<*>): String {
        val main = compilation.name == KotlinCompilation.MAIN_COMPILATION_NAME
        val part = when {
            main && compilation.target.name in JsTargets -> "js"
            main -> compilation.target.name
            else -> compilation.defaultSourceSet.name
        }
        return "ksp" + part.replaceFirstChar(Char::uppercase)
    }

    private fun eachAndroidConfiguration(project: Project, target: KotlinTarget, action: (String) -> Unit) {
        val prefix = "ksp" + target.name.replaceFirstChar(Char::uppercase)
        project.configurations.matching { configuration ->
            configuration.name.startsWith(prefix) && configuration.description.orEmpty().endsWith(AndroidSourceSet)
        }.configureEach { action(it.name) }
    }

    private fun eachConfiguration(project: Project, name: String, action: (String) -> Unit) {
        project.configurations.matching { it.name == name }.configureEach { action(it.name) }
    }

    companion object {
        private val JsTargets = setOf("jsLegacy", "jsIr")
        private const val AndroidSourceSet = "(Android)' source set."

        @Include
        val KspConfigurationsOfCompilations.asConfigurations: KspConfigurations
            get() = this
    }
}
