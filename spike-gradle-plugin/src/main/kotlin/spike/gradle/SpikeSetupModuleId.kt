package spike.gradle

import org.gradle.api.Project

internal class SpikeSetupModuleId(
    private val tasks: KspTasks,
    private val argument: (String) -> SpikeModuleArgument,
) : SpikeSetup {
    override fun apply(project: Project) {
        tasks.each(
            project,
            compiled = { compilation, task ->
                val module = if (compilation.name == MainCompilation) project.path else "${project.path}<${compilation.name}>"
                task.commandLineArgumentProviders.add(argument(module))
            },
            orphan = { task ->
                project.logger.warn("Spike found no Kotlin compilation for ${task.path}; its module id is the task path.")
                task.commandLineArgumentProviders.add(argument("${project.path}<${task.name}>"))
            },
        )
    }

    private companion object {
        const val MainCompilation = "main"
    }
}
