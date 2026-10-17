package spike.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project

public class SpikePlugin : Plugin<Project> {

    override fun apply(project: Project) {
        SpikeSetups().setup.apply(project)
    }
}
