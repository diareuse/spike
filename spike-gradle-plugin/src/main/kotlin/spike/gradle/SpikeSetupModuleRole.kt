package spike.gradle

import com.google.devtools.ksp.gradle.KspExtension
import org.gradle.api.Project

internal class SpikeSetupModuleRole(private val applicationPlugins: List<String>) : SpikeSetup {
    override fun apply(project: Project) {
        val library = project.provider {
            applicationPlugins.none(project.pluginManager::hasPlugin).toString()
        }
        project.extensions.getByType(KspExtension::class.java).arg("spike.library", library)
    }
}
