package spike.gradle

import com.google.devtools.ksp.gradle.KspAATask
import org.gradle.api.Project
import org.gradle.api.provider.Provider

internal class SpikeSetupLibraryRole(
    private val compilations: KspCompilations,
    private val argument: (Provider<Boolean>) -> SpikeLibraryArgument,
) : SpikeSetup {
    override fun apply(project: Project) {
        val extension = project.extensions.getByType(SpikeExtension::class.java)
        val library = argument(extension.library.orElse(false))
        project.tasks.withType(KspAATask::class.java).configureEach { task ->
            val compilation = compilations.byTask(project)[task.name]
            if (compilation == null) {
                if (extension.library.getOrElse(false)) {
                    project.logger.warn("spike { library = true } does not apply to ${task.path}: no Kotlin compilation runs it.")
                }
                return@configureEach
            }
            if (compilation.name == TestCompilation || compilation.name.endsWith(TestSuffix)) return@configureEach
            task.commandLineArgumentProviders.add(library)
        }
    }

    private companion object {
        const val TestCompilation = "test"
        const val TestSuffix = "Test"
    }
}
