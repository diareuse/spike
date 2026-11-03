package spike.gradle

import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinSingleTargetExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinCompilation
import spike.Include

@Include
internal class KspCompilationsByTaskName : KspCompilations {
    private val computed = mutableMapOf<String, Map<String, KotlinCompilation<*>>>()

    override fun byTask(project: Project): Map<String, KotlinCompilation<*>> = computed.getOrPut(project.path) {
        compilations(project).associateBy { "ksp" + it.compileKotlinTaskName.removePrefix("compile") }
    }

    private fun compilations(project: Project): List<KotlinCompilation<*>> =
        when (val kotlin = project.extensions.findByName("kotlin")) {
            is KotlinMultiplatformExtension -> kotlin.targets.flatMap { it.compilations }
            is KotlinSingleTargetExtension<*> -> kotlin.target.compilations.toList()
            else -> emptyList()
        }

    companion object {
        @Include
        val KspCompilationsByTaskName.asCompilations: KspCompilations
            get() = this
    }
}
