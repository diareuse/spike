package spike.gradle

import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

internal class SpikeSetupMultiplatform(
    private val artifacts: SpikeArtifacts,
    private val configurations: KspConfigurations,
) : SpikeSetup {
    override fun apply(project: Project) {
        project.pluginManager.withPlugin("org.jetbrains.kotlin.multiplatform") {
            val kotlin = project.extensions.getByType(KotlinMultiplatformExtension::class.java)
            kotlin.sourceSets.named("commonMain").configure { sourceSet ->
                sourceSet.dependencies { implementation(artifacts.runtime) }
            }
            configurations.each(project) { configuration ->
                project.dependencies.add(configuration, artifacts.compiler)
            }
        }
    }
}
