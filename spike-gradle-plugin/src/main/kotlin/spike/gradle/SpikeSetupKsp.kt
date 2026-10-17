package spike.gradle

import org.gradle.api.Project

internal class SpikeSetupKsp : SpikeSetup {
    override fun apply(project: Project) {
        project.pluginManager.apply("com.google.devtools.ksp")
    }
}
