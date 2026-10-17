package spike.compiler.processor

import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSFile
import com.squareup.kotlinpoet.AnnotationSpec
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.joinToCode
import com.squareup.kotlinpoet.ksp.addOriginatingKSFile
import com.squareup.kotlinpoet.ksp.writeTo
import spike.InternalSpikeApi
import spike.compiler.generator.findKSFile
import spike.compiler.processor.symbol.SymbolRegistry
import spike.factory.IncludedDeclarations
import spike.BindTarget
import spike.Include
import spike.compiler.Order

@OptIn(InternalSpikeApi::class)
@Include
class GraphContributorIndex(
    private val registry: SymbolRegistry,
    private val environment: SymbolProcessorEnvironment,
) : GraphContributor {
    override fun contribute(context: GraphContext) {
        val includes = registry.include(context.resolver).toList()
        val names = includes.mapNotNull(EntryPointIndex::nameOf).sorted()
        val files = includes.mapNotNull { it.findKSFile() }.distinct()
        for (entryPoint in registry.entryPoint(context.resolver)) write(entryPoint, names, files)
    }

    private fun write(
        entryPoint: KSClassDeclaration,
        names: List<String>,
        files: List<KSFile>,
    ) {
        val name = entryPoint.qualifiedName?.asString() ?: return
        val className = EntryPointIndex.classNameOf(name)
        val type = TypeSpec.objectBuilder(className)
            .addAnnotation(
                AnnotationSpec.builder(IncludedDeclarations::class)
                    .addMember("names = [%L]", names.map { CodeBlock.of("%S", it) }.joinToCode())
                    .build()
            )
        for (file in files + listOfNotNull(entryPoint.findKSFile())) type.addOriginatingKSFile(file)
        FileSpec.builder(className)
            .addAnnotation(
                AnnotationSpec.builder(ClassName("kotlin", "OptIn"))
                    .useSiteTarget(AnnotationSpec.UseSiteTarget.FILE)
                    .addMember("%T::class", InternalSpikeApi::class)
                    .build()
            )
            .addAnnotation(
                AnnotationSpec.builder(Suppress::class)
                    .useSiteTarget(AnnotationSpec.UseSiteTarget.FILE)
                    .addMember("%S", "ClassName")
                    .build()
            )
            .addType(type.build())
            .build()
            .writeTo(environment.codeGenerator, true)
    }

    companion object {
        @Include(bindTo = BindTarget.Map)
        @Order(40)
        val GraphContributorIndex.asGraphContributor: GraphContributor
            get() = this
    }
}
