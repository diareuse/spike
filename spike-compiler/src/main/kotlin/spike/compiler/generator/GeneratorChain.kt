package spike.compiler.generator

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.TypeSpec

typealias FileGeneratorChain<T> = GeneratorChain<T, FileSpec.Builder>
typealias TypeGeneratorChain<T> = GeneratorChain<T, TypeSpec.Builder>
typealias CodeBlockGeneratorChain<T> = GeneratorChain<T, CodeBlock.Builder>

interface GeneratorChain<Subject, Builder> {
    val spec: Builder
    val resolver: TypeResolver
    val subject: Subject
    fun proceed(origin: Generator<Subject, Builder>): Builder

    companion object {
        fun <Subject, Builder> create(
            subject: Subject,
            resolver: TypeResolver,
            spec: Builder,
            vararg generators: Generator<Subject, Builder>
        ): GeneratorChainOrigin<Subject, Builder> = GeneratorChainGeneric(
            subject = subject,
            resolver = resolver,
            spec = spec,
            generators = generators
        )

        fun <Subject> classType(
            subject: Subject,
            resolver: TypeResolver,
            name: ClassName,
            vararg generators: Generator<Subject, TypeSpec.Builder>
        ): GeneratorChainOrigin<Subject, TypeSpec.Builder> = create(
            subject = subject,
            resolver = resolver,
            spec = TypeSpec.classBuilder(name),
            generators = generators
        )

        fun <Subject> objectType(
            subject: Subject,
            resolver: TypeResolver,
            name: ClassName,
            vararg generators: Generator<Subject, TypeSpec.Builder>
        ): GeneratorChainOrigin<Subject, TypeSpec.Builder> = create(
            subject = subject,
            resolver = resolver,
            spec = TypeSpec.objectBuilder(name),
            generators = generators
        )

        fun <Subject> file(
            subject: Subject,
            resolver: TypeResolver,
            name: ClassName,
            type: GeneratorChainOrigin<Subject, TypeSpec.Builder>,
            vararg generators: Generator<Subject, FileSpec.Builder>
        ): GeneratorChainOrigin<Subject, FileSpec.Builder> = create(
            subject = subject,
            resolver = resolver,
            spec = FileSpec.builder(name),
            generators = arrayOf(GeneratorFileWithType(type), *generators)
        )

        fun <Subject> codeBlock(
            subject: Subject,
            resolver: TypeResolver,
            vararg generators: Generator<Subject, CodeBlock.Builder>
        ): GeneratorChainOrigin<Subject, CodeBlock.Builder> = create(
            subject = subject,
            resolver = resolver,
            spec = CodeBlock.builder(),
            generators = generators
        )
    }
}

private class GeneratorChainGeneric<Subject, Builder>(
    override val subject: Subject,
    override val resolver: TypeResolver,
    override val spec: Builder,
    private val generators: Array<out Generator<Subject, Builder>>
) : GeneratorChainOrigin<Subject, Builder> {
    override fun proceed() = generators[0].generate(this)
    override fun proceed(origin: Generator<Subject, Builder>): Builder {
        val index = generators.indexOf(origin) + 1
        if (index == generators.size) return spec
        return generators[index].generate(this)
    }
}
