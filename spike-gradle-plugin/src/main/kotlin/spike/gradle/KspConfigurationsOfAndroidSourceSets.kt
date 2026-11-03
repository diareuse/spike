package spike.gradle

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Project
import spike.Include

@Include
internal class KspConfigurationsOfAndroidSourceSets(private val targets: KotlinTargets) : KspConfigurations {
    override fun each(project: Project, action: (String) -> Unit) {
        targets.each(project) { target ->
            if (!target.usesAndroidSourceSets()) return@each
            project.pluginManager.withPlugin(AndroidBasePlugin) {
                val android = project.extensions.getByType(CommonExtension::class.java)
                android.sourceSets.configureEach { sourceSet ->
                    val name = nameOf(target.name, sourceSet.name)
                    project.configurations.matching { it.name == name }.configureEach { action(it.name) }
                }
            }
        }
    }

    private fun nameOf(target: String, sourceSet: String): String {
        val part = if (sourceSet.endsWith(MainSourceSet, ignoreCase = true)) sourceSet.dropLast(MainSourceSet.length) else sourceSet
        return listOf("ksp", target, part).joinToString("") { it.replaceFirstChar(Char::uppercase) }
            .replaceFirstChar(Char::lowercase)
    }

    private companion object {
        const val AndroidBasePlugin = "com.android.base"
        const val MainSourceSet = "main"
    }
}
