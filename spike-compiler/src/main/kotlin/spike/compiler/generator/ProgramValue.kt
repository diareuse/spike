package spike.compiler.generator

import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.TypeSpec

/**
 * A value that the parts of a program share besides the dependency factory, set up once by `resolve`.
 */
interface ProgramValue {
    val parameter: ParameterSpec

    fun declare(program: TypeSpec.Builder, resolve: FunSpec.Builder, body: CodeBlock.Builder)
}

/**
 * Decides whether a program needs a [ProgramValue], and builds it when it does.
 */
fun interface ProgramValueSource {
    fun of(program: LinearProgram, layout: LinearLayout, guards: LinearGuards): ProgramValue?
}
