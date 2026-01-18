package spike.compiler.generator.entrypoint

import com.squareup.kotlinpoet.*
import spike.compiler.generator.FileGenerator
import spike.compiler.generator.FileGeneratorChain
import spike.graph.GraphEntryPoint

class EntryPointFileInitializer : FileGenerator<GraphEntryPoint> {
    override fun generate(chain: FileGeneratorChain<GraphEntryPoint>): FileSpec.Builder {
        val entryPointClass = chain.resolver.getTypeName(chain.subject.type) as ClassName
        val entryPointCompanion = entryPointClass.nestedClass("Companion")
        val initializer = FunSpec.builder("invoke")
            .addModifiers(KModifier.OPERATOR)
            .receiver(entryPointCompanion)
            .returns(entryPointClass)
        val body = CodeBlock.builder()
            .add(
                "return %T().%N(",
                chain.resolver.transformClassName(chain.subject.factory.type),
                chain.subject.factory.method.name
            )
        for ((index, param) in chain.subject.factory.method.parameters.withIndex()) {
            if (index > 0) body.add(", ")
            body.add("%N", param.name)
            initializer.addParameter(param.name, chain.resolver.getTypeName(param.type))
        }
        body.add(")")
        initializer.addCode(body.build())
        chain.spec.addFunction(initializer.build())
        return chain.proceed()
    }
}