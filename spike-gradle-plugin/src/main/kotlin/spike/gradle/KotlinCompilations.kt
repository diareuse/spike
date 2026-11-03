package spike.gradle

import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.plugin.KotlinCompilation

internal fun interface KotlinCompilations {
    fun each(project: Project, action: (KotlinCompilation<*>) -> Unit)
}
