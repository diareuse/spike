package spike.compiler.generator

import com.squareup.kotlinpoet.CodeBlock
import spike.Include
import spike.compiler.generator.code.addArgument
import spike.compiler.generator.code.addCall
import spike.compiler.generator.code.addDependencyFactoryCall
import spike.compiler.generator.code.addLazy
import spike.compiler.generator.code.addMap
import spike.compiler.generator.code.addOptionalSingleton
import spike.compiler.generator.code.addParameters
import spike.compiler.generator.code.addProvider
import spike.compiler.generator.code.addType
import spike.compiler.generator.code.mapEntries
import spike.compiler.graph.Type
import spike.compiler.graph.TypeFactory.Class
import spike.compiler.graph.TypeFactory.External
import spike.compiler.graph.TypeFactory.Imported
import spike.compiler.graph.TypeFactory.Memorizes
import spike.compiler.graph.TypeFactory.Method
import spike.compiler.graph.TypeFactory.MultibindsCollection
import spike.compiler.graph.TypeFactory.MultibindsMap
import spike.compiler.graph.TypeFactory.Property
import spike.compiler.graph.TypeFactory.Provides

@Suppress("TooManyFunctions")
@Include
class TypeFactoryExpression {

    context(context: FileGeneratorContext, step: LinearStep, references: LinearReferences)
    fun write(body: CodeBlock.Builder) = context.apply {
        when (val factory = step.factory) {
            is Class -> generateClassCase(body, factory)
            is Method -> generateMethodCase(body, factory)
            is Memorizes -> generateMemorizesCase(body, factory)
            is Provides -> generateProvidesCase(body, factory)
            is MultibindsCollection -> generateMultibindsCollectionCase(body, factory)
            is MultibindsMap -> generateMultibindsMapCase(body, factory)
            is Property -> generatePropertyCase(body, factory)
            is External -> generateExternalCase(body, factory)
            is Imported -> generateImportedCase(body, factory)
        }
    }

    context(step: LinearStep, references: LinearReferences)
    private fun FileGeneratorContext.generateClassCase(
        body: CodeBlock.Builder,
        factory: Class,
    ) {
        body.addOptionalSingleton(getDependencyId(factory), factory.singleton) {
            addType(factory.type) {
                body.addParameters(factory.invocation)
            }.addStatement("")
        }
    }

    context(step: LinearStep, references: LinearReferences)
    private fun FileGeneratorContext.generateMethodCase(
        body: CodeBlock.Builder,
        factory: Method,
    ) {
        body.addOptionalSingleton(getDependencyId(factory), factory.singleton) {
            body.addCall(factory.member, factory.invocation).addStatement("")
        }
    }

    context(step: LinearStep, references: LinearReferences)
    private fun FileGeneratorContext.generateMemorizesCase(body: CodeBlock.Builder, factory: Memorizes) {
        body.addLazy {
            addDependencyFactoryCall(factory.factory)
        }.addStatement("")
    }

    context(step: LinearStep, references: LinearReferences)
    private fun FileGeneratorContext.generateProvidesCase(body: CodeBlock.Builder, factory: Provides) {
        body.addProvider {
            addDependencyFactoryCall(factory.factory)
        }.addStatement("")
    }

    context(step: LinearStep, references: LinearReferences)
    private fun FileGeneratorContext.generateMultibindsCollectionCase(
        body: CodeBlock.Builder,
        factory: MultibindsCollection
    ) {
        body.add(
            "%M<%T>(",
            resolver.getMemberName(factory.collectionMemberFactory),
            resolver.getTypeName((factory.type as Type.Parametrized).typeArguments.single()),
        )
        body.addParameters(factory.entries)
        body.add(")").addStatement("")
    }

    context(step: LinearStep, references: LinearReferences)
    private fun FileGeneratorContext.generateMultibindsMapCase(
        body: CodeBlock.Builder,
        factory: MultibindsMap
    ) {
        val value = factory.type.typeArguments[1]
        body.addMap(key = factory.type.typeArguments[0], value = value) {
            mapEntries(factory.keyValues.entries, value)
        }.addStatement("")
    }

    context(step: LinearStep, references: LinearReferences)
    private fun FileGeneratorContext.generatePropertyCase(body: CodeBlock.Builder, factory: Property) {
        body.addStatement("factory._%L", factory.name)
    }

    context(step: LinearStep, references: LinearReferences)
    private fun FileGeneratorContext.generateExternalCase(body: CodeBlock.Builder, factory: External) {
        val call = if (factory.isMethod) "()" else ""
        body.addStatement("factory.%L.%L%L", resolver.getVariableName(factory.origin), factory.name, call)
    }

    context(step: LinearStep, references: LinearReferences)
    private fun FileGeneratorContext.generateImportedCase(body: CodeBlock.Builder, factory: Imported) {
        body.addStatement("factory.%L", factory.name)
    }
}
