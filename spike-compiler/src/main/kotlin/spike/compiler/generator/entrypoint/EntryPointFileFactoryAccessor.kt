package spike.compiler.generator.entrypoint

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.PropertySpec
import spike.compiler.generator.FileGenerator
import spike.compiler.generator.FileGeneratorChain
import spike.compiler.graph.GraphEntryPoint

class EntryPointFileFactoryAccessor : FileGenerator<GraphEntryPoint> {
    override fun generate(chain: FileGeneratorChain<GraphEntryPoint>): FileSpec.Builder {
        if (chain.subject.factory.isVirtual) return chain.proceed()
        val entryPointClass = chain.resolver.getTypeName(chain.subject.type) as ClassName
        val entryPointCompanion = entryPointClass.nestedClass("Companion")
        val entryPointFactoryClass = chain.resolver.getTypeName(chain.subject.factory.type) as ClassName
        val initializer = FunSpec.getterBuilder()
            .addStatement(
                "return %T",
                chain.resolver.transformClassName(chain.subject.factory.type)
            )
        val property = PropertySpec
            .builder("factory", entryPointFactoryClass)
            .receiver(entryPointCompanion)
            .getter(initializer.build())
        chain.spec.addProperty(property.build())
        return chain.proceed()
    }
}