package spike.compiler.generator

class GeneratorChainGeneric<Subject, Builder>(
    override val subject: Subject,
    private val generators: List<Generator<Subject, Builder>>,
    override val resolver: TypeResolver,
    override val spec: Builder
): GeneratorChainOrigin<Subject, Builder> {
    override fun proceed() = generators[0].generate(this)
    override fun proceed(origin: Generator<Subject, Builder>): Builder {
        val index = generators.indexOf(origin) + 1
        if (index == generators.size) return spec
        return generators[index].generate(this)
    }
}
