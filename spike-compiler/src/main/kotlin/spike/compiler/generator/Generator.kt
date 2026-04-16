package spike.compiler.generator

fun interface Generator {
    fun generate(context: FileGeneratorContext, collector: FileSpecCollector)
}
