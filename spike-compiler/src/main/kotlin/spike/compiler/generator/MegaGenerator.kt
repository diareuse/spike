package spike.compiler.generator

import com.squareup.kotlinpoet.FileSpec

class MegaGenerator(
    private val context: FileGeneratorContext,
    private val entryPoint: EntryPointGenerator,
    private val dependencyFactory: DependencyFactoryGenerator
) {

    fun generate(): List<FileSpec> = buildList {
        dependencyFactory.generate(context, ::add)
        entryPoint.generate(context, ::add)
    }


}