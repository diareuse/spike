package spike.compiler.conversion

import com.google.devtools.ksp.symbol.KSAnnotated
import spike.compiler.conversion.AnnotationNames

class ScopeLookupAnnotation(
    private val singleton: AnnotationNames,
    private val scope: AnnotationNames,
) : ScopeLookup {
    override fun isScoped(node: KSAnnotated) = node.annotations.any { annotation ->
        singleton.matches(annotation) || scope.annotatesAnnotationOf(annotation)
    }
}
