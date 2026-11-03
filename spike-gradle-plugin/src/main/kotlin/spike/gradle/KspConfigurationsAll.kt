package spike.gradle

import org.gradle.api.Project

internal class KspConfigurationsAll(private val configurations: List<KspConfigurations>) : KspConfigurations {
    override fun each(project: Project, action: (String) -> Unit) {
        for (configuration in configurations) configuration.each(project, action)
    }
}
