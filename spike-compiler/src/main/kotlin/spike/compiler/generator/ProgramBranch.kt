package spike.compiler.generator

import com.squareup.kotlinpoet.ClassName

/**
 * The program that builds the dependency with identifier [id].
 */
class ProgramBranch(val id: Int, val program: ClassName)
