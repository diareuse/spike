package spike.compiler.processor

import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSFunctionDeclaration

class IncludeContributorMultiplatform(
    private val origin: IncludeContributor,
    private val resolver: Resolver
) : IncludeContributor {
    override fun contribute(context: GraphContext, annotated: KSClassDeclaration) {
        if (annotated.isExpect) {
            val declaration = resolver.getClassDeclarationByName(annotated.qualifiedName!!)
            checkNotNull(declaration) {
                "Expect class $annotated was annotated with @Include, but actual members couldn't be found"
            }
            return origin.contribute(context, declaration)
        }
        return origin.contribute(context, annotated)
    }

    override fun contribute(context: GraphContext, annotated: KSFunctionDeclaration) {
        if (annotated.isExpect) {
            val member = resolver.getFunctionDeclarationsByName(annotated.qualifiedName!!, true)
                .filter { it.isActual }
                .firstOrNull {
                    it.parameters.size == annotated.parameters.size && it.parameters
                        .zip(annotated.parameters)
                        .all { (a, b) -> a.type.resolve() == b.type.resolve() }
                }
            checkNotNull(member) {
                "Expect function $annotated was annotated @Include, but matching 'actual' member couldn't be found"
            }
            return origin.contribute(context, annotated)
        }
        return origin.contribute(context, annotated)
    }
}