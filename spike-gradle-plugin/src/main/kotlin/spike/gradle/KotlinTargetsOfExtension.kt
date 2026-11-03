package spike.gradle

import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinSingleTargetExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinTarget
import spike.Include

@Include
internal class KotlinTargetsOfExtension : KotlinTargets {
    override fun each(project: Project, action: (KotlinTarget) -> Unit) {
        when (val kotlin = project.extensions.findByName("kotlin")) {
            is KotlinMultiplatformExtension -> kotlin.targets.configureEach(action)
            is KotlinSingleTargetExtension<*> -> action(kotlin.target)
        }
    }

    companion object {
        @Include
        val KotlinTargetsOfExtension.asTargets: KotlinTargets
            get() = this
    }
}
