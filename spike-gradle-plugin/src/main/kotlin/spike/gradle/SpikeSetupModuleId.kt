package spike.gradle

import com.google.devtools.ksp.gradle.KspAATask
import org.gradle.api.Project

internal class SpikeSetupModuleId(
    private val compilations: KspCompilations,
    private val argument: (String) -> SpikeModuleArgument,
) : SpikeSetup {
    override fun apply(project: Project) {
        project.tasks.withType(KspAATask::class.java).configureEach { task ->
            val compilation = compilations.byTask(project)[task.name]?.name ?: MainCompilation
            val module = if (compilation == MainCompilation) project.path else "${project.path}<$compilation>"
            task.commandLineArgumentProviders.add(argument(module))
        }
    }

    private companion object {
        const val MainCompilation = "main"
    }
}
