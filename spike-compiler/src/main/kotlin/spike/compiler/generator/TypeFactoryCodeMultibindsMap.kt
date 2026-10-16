package spike.compiler.generator

import com.squareup.kotlinpoet.CodeBlock
import spike.compiler.generator.code.addMap
import spike.compiler.generator.code.mapEntries
import spike.compiler.graph.TypeFactory

class TypeFactoryCodeMultibindsMap : TypeFactoryCode<TypeFactory.MultibindsMap> {
    context(context: FileGeneratorContext, step: LinearStep, references: LinearReferences)
    override fun write(body: CodeBlock.Builder, factory: TypeFactory.MultibindsMap) {
        val value = factory.type.typeArguments[1]
        body.addMap(key = factory.type.typeArguments[0], value = value) {
            mapEntries(factory.keyValues.entries, value)
        }.addStatement("")
    }
}
