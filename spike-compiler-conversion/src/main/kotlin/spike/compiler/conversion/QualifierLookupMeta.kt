package spike.compiler.conversion

import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSAnnotation
import spike.compiler.conversion.AnnotationNames

class QualifierLookupMeta(
    private val qualifier: AnnotationNames,
    private val converted: AnnotationNames,
) : QualifierLookup {
    override fun unconvertible(node: KSAnnotated): KSAnnotation? = node.annotations.firstOrNull { annotation ->
        !converted.matches(annotation) && qualifier.annotatesAnnotationOf(annotation)
    }
}
