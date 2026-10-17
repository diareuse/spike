package spike.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project

public class SpikePlugin : Plugin<Project> {

    override fun apply(project: Project) {
        val artifacts = SpikeArtifacts(SpikeVersionResource().value())
        val setup = SpikeSetupAll(
            listOf(
                SpikeSetupKsp(),
                SpikeSetupMultiplatform(artifacts),
                SpikeSetupSinglePlatform("org.jetbrains.kotlin.jvm", artifacts),
                SpikeSetupSinglePlatform("org.jetbrains.kotlin.android", artifacts),
            ),
        )
        setup.apply(project)
    }
}
