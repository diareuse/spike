package spike.compiler.conversion

import com.squareup.kotlinpoet.FunSpec

/**
 * Builds the @Include function that replaces [T] in the Spike graph.
 */
fun interface IncludeFunction<in T : Declared> {
    fun of(subject: T): FunSpec
}
