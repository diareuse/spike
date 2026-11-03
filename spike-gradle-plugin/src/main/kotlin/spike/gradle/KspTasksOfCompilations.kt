package spike.gradle

import com.google.devtools.ksp.gradle.KspAATask
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.plugin.KotlinCompilation
import spike.Include

@Include
internal class KspTasksOfCompilations(private val compilations: KotlinCompilations) : KspTasks {
    override fun each(
        project: Project,
        compiled: (KotlinCompilation<*>, KspAATask) -> Unit,
        orphan: (KspAATask) -> Unit,
    ) {
        val claimed = mutableSetOf<String>()
        compilations.each(project) { compilation ->
            val name = "ksp" + compilation.compileKotlinTaskName.removePrefix("compile")
            claimed += name
            project.tasks.withType(KspAATask::class.java).matching { it.name == name }.configureEach { task ->
                compiled(compilation, task)
            }
        }
        project.tasks.withType(KspAATask::class.java).configureEach { task ->
            if (task.name !in claimed) orphan(task)
        }
    }

    companion object {
        @Include
        val KspTasksOfCompilations.asTasks: KspTasks
            get() = this
    }
}
