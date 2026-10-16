package spike.compiler.generator

import com.squareup.kotlinpoet.AnnotationSpec
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.TypeVariableName
import spike.factory.DependencyId

class DispatchGet : DispatchFunctions {
    override fun of(branches: List<ProgramBranch>): List<FunSpec> {
        val block = CodeBlock.builder().beginControlFlow("return when (id.%L) {", DependencyId::id.name)
        for (branch in branches) block.addStatement("%L -> %T.resolve(this)", branch.id, branch.program)
        block.addStatement($$"else -> error(\"Invalid identifier $id\")")
        block.unindent().add("} as T\n")
        return listOf(getFunction(block.build()))
    }

    companion object {
        fun getFunction(body: CodeBlock): FunSpec = FunSpec.builder("get")
            .addAnnotation(AnnotationSpec.builder(Suppress::class).addMember("%S", "UNCHECKED_CAST").build())
            .addTypeVariable(TypeVariableName("T"))
            .returns(TypeVariableName("T"))
            .addParameter("id", DependencyId::class)
            .addCode(body)
            .build()
    }
}
