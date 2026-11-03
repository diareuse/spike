package spike.gradle

import org.gradle.api.Project

internal class SpikeSetupExtension : SpikeSetup {
    override fun apply(project: Project) {
        project.extensions.create("spike", SpikeExtension::class.java)
    }
}
