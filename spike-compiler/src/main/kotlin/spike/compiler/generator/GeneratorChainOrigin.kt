package spike.compiler.generator

interface GeneratorChainOrigin<Subject, Builder> : GeneratorChain<Subject, Builder> {
    fun proceed(): Builder
}
