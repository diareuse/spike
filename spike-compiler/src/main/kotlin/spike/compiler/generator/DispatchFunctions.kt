package spike.compiler.generator

import com.squareup.kotlinpoet.FunSpec

/**
 * Builds the functions of a dependency factory that route an identifier to the program building it.
 */
fun interface DispatchFunctions {
    fun of(branches: List<ProgramBranch>): List<FunSpec>
}
