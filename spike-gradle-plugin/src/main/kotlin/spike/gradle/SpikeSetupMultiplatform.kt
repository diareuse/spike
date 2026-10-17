package spike.gradle

import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinPlatformType

internal class SpikeSetupMultiplatform(private val artifacts: SpikeArtifacts) : SpikeSetup {
    override fun apply(project: Project) {
        project.pluginManager.withPlugin("org.jetbrains.kotlin.multiplatform") {
            val kotlin = project.extensions.getByType(KotlinMultiplatformExtension::class.java)
            kotlin.sourceSets.named("commonMain").configure { sourceSet ->
                sourceSet.dependencies { implementation(artifacts.runtime) }
            }
            kotlin.targets.configureEach { target ->
                if (target.platformType == KotlinPlatformType.common) return@configureEach
                val configuration = "ksp" + target.name.replaceFirstChar(Char::uppercase)
                project.configurations.matching { it.name == configuration }.configureEach {
                    project.dependencies.add(configuration, artifacts.compiler)
                }
            }
        }
    }
}
