package spike.gradle

import com.google.devtools.ksp.gradle.KspAATask
import org.gradle.api.Project

internal class SpikeSetupLibraryRole : SpikeSetup {
    override fun apply(project: Project) {
        val extension = project.extensions.create("spike", SpikeExtension::class.java)
        val argument = SpikeLibraryArgument(extension.library.orElse(false))
        project.tasks.withType(KspAATask::class.java).configureEach { task ->
            if (TestCompilation in task.name) return@configureEach
            task.commandLineArgumentProviders.add(argument)
        }
    }

    private companion object {
        const val TestCompilation = "Test"
    }
}
