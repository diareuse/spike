package spike.gradle

import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinSingleTargetExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinCompilation
import spike.Include

@Include
internal class KotlinCompilationsOfExtension : KotlinCompilations {
    override fun each(project: Project, action: (KotlinCompilation<*>) -> Unit) {
        when (val kotlin = project.extensions.findByName("kotlin")) {
            is KotlinMultiplatformExtension -> kotlin.targets.configureEach { it.compilations.configureEach(action) }
            is KotlinSingleTargetExtension<*> -> kotlin.target.compilations.configureEach(action)
        }
    }

    companion object {
        @Include
        val KotlinCompilationsOfExtension.asCompilations: KotlinCompilations
            get() = this
    }
}
