package spike.gradle

import org.gradle.api.Project

internal fun interface KspConfigurations {
    fun each(project: Project, action: (String) -> Unit)
}
