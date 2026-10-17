package spike.gradle

import org.gradle.api.Project

internal class SpikeSetupAll(private val setups: List<SpikeSetup>) : SpikeSetup {
    override fun apply(project: Project) {
        for (setup in setups) setup.apply(project)
    }
}
