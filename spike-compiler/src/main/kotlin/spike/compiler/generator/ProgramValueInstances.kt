package spike.compiler.generator

import com.squareup.kotlinpoet.ANY
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.MemberName
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.asClassName

/**
 * The slots through which a program split into parts hands instances from one part to the next.
 */
class ProgramValueInstances : ProgramValueSource {
    override fun of(program: LinearProgram, layout: LinearLayout, guards: LinearGuards): ProgramValue? =
        if (layout.slots.isEmpty()) null else Value(layout.slots.size)

    private class Value(private val size: Int) : ProgramValue {
        override val parameter = ParameterSpec.builder(
            "instances",
            Array::class.asClassName().parameterizedBy(ANY.copy(nullable = true)),
        ).build()

        override fun declare(program: TypeSpec.Builder, resolve: FunSpec.Builder, body: CodeBlock.Builder) {
            body.addStatement("val instances = %M<%T>(%L)", MemberName("kotlin", "arrayOfNulls"), ANY, size)
        }
    }
}
