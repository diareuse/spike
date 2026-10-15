package spike.compiler.processor

import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import com.google.devtools.ksp.symbol.KSPropertyDeclaration

class IncludeContributorBackingField(
    private val origin: IncludeContributor,
) : IncludeContributor {
    override fun contribute(context: GraphContext, annotated: KSClassDeclaration) =
        origin.contribute(context, annotated)

    override fun contribute(context: GraphContext, annotated: KSFunctionDeclaration) =
        origin.contribute(context, annotated)

    override fun contribute(context: GraphContext, annotated: KSPropertyDeclaration) {
        if (annotated.hasBackingField) {
            val name = annotated.simpleName.asString()
            error(
                """@Include property $name has a backing field, so every entry point would share one value:
                    |
                    |<description>
                    |  An entry point owns its singletons. Compute the value in a getter, `val $name get() = ...`,
                    |  and add @spike.Singleton if one instance per entry point is wanted.
                    |</description>""".trimMargin()
            )
        }
        origin.contribute(context, annotated)
    }
}
