package spike.gradle

import com.google.devtools.ksp.gradle.KspExtension
import org.gradle.api.Project

internal class SpikeSetupModuleId : SpikeSetup {
    override fun apply(project: Project) {
        val id = project.path.removePrefix(":").ifEmpty { project.name }
        project.extensions.getByType(KspExtension::class.java).arg("spike.module", id)
    }
}
