package spike.compiler.generator

import com.squareup.kotlinpoet.ANY
import com.squareup.kotlinpoet.AnnotationSpec
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.TypeVariableName
import com.squareup.kotlinpoet.asTypeName
import spike.factory.DependencyId
import spike.Include

/**
 * Routes an identifier and the runtime arguments of an assisted call to the program building that dependency.
 */
@Include
class DispatchAssisted : DispatchFunctions {
    override fun of(branches: List<ProgramBranch>): List<FunSpec> {
        if (branches.isEmpty()) return emptyList()
        val block = CodeBlock.builder().beginControlFlow("return when (id.%L) {", DependencyId::id.name)
        for (branch in branches) block.addStatement("%L -> %T.resolve(this, arguments)", branch.id, branch.program)
        block.addStatement($$"else -> error(\"Invalid identifier $id\")")
        block.unindent().add("} as T\n")
        return listOf(
            FunSpec.builder("assisted")
                .addAnnotation(AnnotationSpec.builder(Suppress::class).addMember("%S", "UNCHECKED_CAST").build())
                .addTypeVariable(TypeVariableName("T"))
                .returns(TypeVariableName("T"))
                .addParameter("id", DependencyId::class)
                .addParameter("arguments", Array::class.asTypeName().parameterizedBy(ANY.copy(nullable = true)))
                .addCode(block.build())
                .build()
        )
    }
}
