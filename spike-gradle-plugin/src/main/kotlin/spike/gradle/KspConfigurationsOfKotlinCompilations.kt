package spike.gradle

import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.plugin.KotlinCompilation
import org.jetbrains.kotlin.gradle.plugin.KotlinPlatformType
import spike.Include

@Include
internal class KspConfigurationsOfKotlinCompilations(private val compilations: KotlinCompilations) : KspConfigurations {
    override fun each(project: Project, action: (String) -> Unit) {
        compilations.each(project) { compilation ->
            val target = compilation.target
            if (target.platformType == KotlinPlatformType.common || target.usesAndroidSourceSets()) return@each
            val name = nameOf(compilation)
            project.configurations.matching { it.name == name }.configureEach { action(it.name) }
        }
    }

    private fun nameOf(compilation: KotlinCompilation<*>): String {
        val main = compilation.name == KotlinCompilation.MAIN_COMPILATION_NAME
        val part = when {
            main && compilation.target.name in JsTargets -> "js"
            main -> compilation.target.name
            else -> compilation.defaultSourceSet.name
        }
        return "ksp" + part.replaceFirstChar(Char::uppercase)
    }

    private companion object {
        val JsTargets = setOf("jsLegacy", "jsIr")
    }
}
