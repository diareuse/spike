package spike.compiler.processor.symbol

import com.google.devtools.ksp.KspExperimental
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.Origin

@OptIn(KspExperimental::class)
class ResolverDeferredView(private val origin: Resolver, deferred: List<KSAnnotated>) : Resolver by origin {
    private val visible = deferred.filterTo(mutableSetOf()) { it.origin == Origin.KOTLIN || it.origin == Origin.JAVA }

    override fun getSymbolsWithAnnotation(annotationName: String, inDepth: Boolean): Sequence<KSAnnotated> =
        origin.getSymbolsWithAnnotation(annotationName, inDepth).filter { it in visible }
}
