package spike.gradle

import org.gradle.api.Project
import org.gradle.api.provider.Provider

internal class SpikeSetupLibraryRole(
    private val tasks: KspTasks,
    private val argument: (Provider<Boolean>) -> SpikeLibraryArgument,
) : SpikeSetup {
    override fun apply(project: Project) {
        val extension = project.extensions.getByType(SpikeExtension::class.java)
        val library = argument(extension.library.orElse(false))
        tasks.each(
            project,
            compiled = { compilation, task ->
                val test = compilation.name == TestCompilation || compilation.name.endsWith(TestSuffix)
                if (!test) task.commandLineArgumentProviders.add(library)
            },
            orphan = { task ->
                if (extension.library.getOrElse(false)) {
                    project.logger.warn("spike { library = true } does not apply to ${task.path}: no Kotlin compilation runs it.")
                }
            },
        )
    }

    private companion object {
        const val TestCompilation = "test"
        const val TestSuffix = "Test"
    }
}
