package spike.compiler.conversion

import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.symbol.KSAnnotated

class SymbolSourceAnnotated(private val names: AnnotationNames) : SymbolSource {
    override fun symbols(resolver: Resolver): List<KSAnnotated> =
        names.all.flatMap { resolver.getSymbolsWithAnnotation(it) }.distinct()
}
