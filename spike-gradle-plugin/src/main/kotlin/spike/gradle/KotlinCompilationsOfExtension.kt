package spike.gradle

import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.plugin.KotlinCompilation
import spike.Include

@Include
internal class KotlinCompilationsOfExtension(private val targets: KotlinTargets) : KotlinCompilations {
    override fun each(project: Project, action: (KotlinCompilation<*>) -> Unit) {
        targets.each(project) { it.compilations.configureEach(action) }
    }

    companion object {
        @Include
        val KotlinCompilationsOfExtension.asCompilations: KotlinCompilations
            get() = this
    }
}
