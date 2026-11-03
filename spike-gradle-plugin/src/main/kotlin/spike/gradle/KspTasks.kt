package spike.gradle

import com.google.devtools.ksp.gradle.KspAATask
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.plugin.KotlinCompilation

internal fun interface KspTasks {
    fun each(project: Project, compiled: (KotlinCompilation<*>, KspAATask) -> Unit, orphan: (KspAATask) -> Unit)
}
