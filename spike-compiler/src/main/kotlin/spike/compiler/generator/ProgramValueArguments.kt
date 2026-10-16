package spike.compiler.generator

import com.squareup.kotlinpoet.ANY
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.asClassName

/**
 * The runtime arguments of an assisted program, passed to `resolve` by the caller.
 */
class ProgramValueArguments : ProgramValueSource {
    override fun of(program: LinearProgram, layout: LinearLayout, guards: LinearGuards): ProgramValue? =
        if (program.takesArguments) Value else null

    private object Value : ProgramValue {
        override val parameter = ParameterSpec.builder(
            "arguments",
            Array::class.asClassName().parameterizedBy(ANY.copy(nullable = true)),
        ).build()

        override fun declare(program: TypeSpec.Builder, resolve: FunSpec.Builder, body: CodeBlock.Builder) {
            resolve.addParameter(parameter)
        }
    }
}
