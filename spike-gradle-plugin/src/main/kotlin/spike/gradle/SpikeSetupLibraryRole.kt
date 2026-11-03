package spike.gradle

import com.google.devtools.ksp.gradle.KspAATask
import org.gradle.api.Project
import org.gradle.api.provider.Provider
import org.gradle.process.CommandLineArgumentProvider
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinSingleTargetExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinCompilation

internal class SpikeSetupLibraryRole(
    private val argument: (Provider<Boolean>) -> CommandLineArgumentProvider,
) : SpikeSetup {
    override fun apply(project: Project) {
        val extension = project.extensions.getByType(SpikeExtension::class.java)
        val library = argument(extension.library.orElse(false))
        project.tasks.withType(KspAATask::class.java).configureEach { task ->
            if (task.name !in productionTasks(project)) return@configureEach
            task.commandLineArgumentProviders.add(library)
        }
    }

    private fun productionTasks(project: Project): Set<String> = compilations(project)
        .filterNot { it.name == TestCompilation || it.name.endsWith(TestSuffix) }
        .mapTo(mutableSetOf()) { "ksp" + it.compileKotlinTaskName.removePrefix("compile") }

    private fun compilations(project: Project): List<KotlinCompilation<*>> =
        when (val kotlin = project.extensions.findByName("kotlin")) {
            is KotlinMultiplatformExtension -> kotlin.targets.flatMap { it.compilations }
            is KotlinSingleTargetExtension<*> -> kotlin.target.compilations.toList()
            else -> emptyList()
        }

    private companion object {
        const val TestCompilation = "test"
        const val TestSuffix = "Test"
    }
}
