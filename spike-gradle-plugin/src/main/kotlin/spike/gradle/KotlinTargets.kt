package spike.gradle

import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.plugin.KotlinTarget

internal fun interface KotlinTargets {
    fun each(project: Project, action: (KotlinTarget) -> Unit)
}
