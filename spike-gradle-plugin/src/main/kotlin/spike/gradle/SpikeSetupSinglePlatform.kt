package spike.gradle

import org.gradle.api.Project

internal class SpikeSetupSinglePlatform(
    private val kotlinPlugin: String,
    private val artifacts: SpikeArtifacts,
    private val configurations: KspConfigurations,
) : SpikeSetup {
    override fun apply(project: Project) {
        project.pluginManager.withPlugin(kotlinPlugin) {
            project.dependencies.add("implementation", artifacts.runtime)
            project.dependencies.add(MainConfiguration, artifacts.compiler)
            configurations.each(project) { configuration ->
                if (configuration != MainConfiguration) project.dependencies.add(configuration, artifacts.compiler)
            }
        }
    }

    private companion object {
        const val MainConfiguration = "ksp"
    }
}
