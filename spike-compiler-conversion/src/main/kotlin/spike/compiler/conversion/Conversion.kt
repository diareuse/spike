package spike.compiler.conversion

/**
 * Writes the Spike declarations that replace [T]. Called only for subjects that passed the converter's checks.
 */
fun interface Conversion<in T> {
    fun convert(subject: T)
}
