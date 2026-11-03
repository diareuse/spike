package spike.gradle

import org.gradle.api.Project

internal class SpikeSetupSinglePlatform(
    private val kotlinPlugin: String,
    private val artifacts: SpikeArtifacts,
) : SpikeSetup {
    override fun apply(project: Project) {
        project.pluginManager.withPlugin(kotlinPlugin) {
            project.dependencies.add("implementation", artifacts.runtime)
            project.dependencies.add("ksp", artifacts.compiler)
            project.configurations.matching { it.name in TestConfigurations }.configureEach { configuration ->
                project.dependencies.add(configuration.name, artifacts.compiler)
            }
        }
    }

    private companion object {
        val TestConfigurations = setOf("kspTest", "kspAndroidTest", "kspTestFixtures")
    }
}
