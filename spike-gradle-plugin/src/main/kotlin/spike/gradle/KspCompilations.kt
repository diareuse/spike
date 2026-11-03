package spike.gradle

import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.plugin.KotlinCompilation

internal fun interface KspCompilations {
    fun byTask(project: Project): Map<String, KotlinCompilation<*>>
}
