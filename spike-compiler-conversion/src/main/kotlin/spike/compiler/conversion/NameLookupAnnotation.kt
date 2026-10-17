package spike.compiler.conversion

import com.google.devtools.ksp.symbol.KSAnnotated
import spike.compiler.conversion.AnnotationNames

class NameLookupAnnotation(private val named: AnnotationNames) : NameLookup {
    override fun name(node: KSAnnotated): String? = node.annotations
        .firstOrNull(named::matches)
        ?.arguments?.firstOrNull()?.value as? String
}
