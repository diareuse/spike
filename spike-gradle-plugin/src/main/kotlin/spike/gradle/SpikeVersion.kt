package spike.gradle

/**
 * The version of Spike that this plugin adds to a project.
 */
internal fun interface SpikeVersion {
    fun value(): String
}
