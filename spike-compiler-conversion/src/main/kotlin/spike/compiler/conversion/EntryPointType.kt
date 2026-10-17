package spike.compiler.conversion

import com.squareup.kotlinpoet.TypeSpec

/**
 * Builds the Spike entry point that replaces a component of another framework.
 */
fun interface EntryPointType<in T : EntryPointSubject> {
    fun of(subject: T): TypeSpec
}
