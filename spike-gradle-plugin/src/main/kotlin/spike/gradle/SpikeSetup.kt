package spike.gradle

import org.gradle.api.Project

/**
 * Adds part of Spike to a project, once the Kotlin plugin it serves is applied.
 */
internal fun interface SpikeSetup {
    fun apply(project: Project)
}
