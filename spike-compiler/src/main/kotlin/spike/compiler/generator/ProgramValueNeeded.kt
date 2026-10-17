package spike.compiler.generator

import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec
import spike.Include
import spike.BindTarget
import spike.compiler.Order

/**
 * Which guarded steps still have to run, read from the guard table against the singletons already cached.
 */
@Include
class ProgramValueNeeded : ProgramValueSource {
    override fun of(program: LinearProgram, layout: LinearLayout, guards: LinearGuards): ProgramValue? =
        if (guards.table.isEmpty()) null else Value(guards.table)

    private class Value(private val table: String) : ProgramValue {
        override val parameter = ParameterSpec.builder("needed", BooleanArray::class).build()

        override fun declare(program: TypeSpec.Builder, resolve: FunSpec.Builder, body: CodeBlock.Builder) {
            program.addProperty(
                PropertySpec.builder("guards", String::class, KModifier.PRIVATE, KModifier.CONST)
                    .initializer("%S", table)
                    .build()
            )
            body.addStatement("val needed = factory._singletons.needed(guards)")
        }
    }

    companion object {
        @Include(bindTo = BindTarget.Map)
        @Order(30)
        val ProgramValueNeeded.asSource: ProgramValueSource
            get() = this
    }
}
