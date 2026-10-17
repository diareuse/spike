package spike.compiler.processor

import com.google.devtools.ksp.KspExperimental
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import spike.compiler.diagnostic.DiagnosticCollector
import spike.compiler.generator.requireKSFile
import spike.compiler.processor.symbol.SymbolRegistry
import spike.BindTarget
import spike.Include
import spike.compiler.Order

@OptIn(KspExperimental::class)
@Include
class GraphContributorInclude(
    private val contributor: IncludeContributor,
    private val registry: SymbolRegistry
) : GraphContributor {
    override fun contribute(context: GraphContext) {
        val diagnostics = DiagnosticCollector()
        for (ks in registry.include(context.resolver)) diagnostics.attempt { contribute(context, ks) }
        diagnostics.throwCollected()
    }

    private fun contribute(context: GraphContext, ks: KSAnnotated) {
        when (ks) {
            is KSClassDeclaration -> contributor.contribute(context, ks)
            is KSFunctionDeclaration -> contributor.contribute(context, ks)
            is KSPropertyDeclaration -> contributor.contribute(context, ks)
            else -> error(
                """@spike.Include // <-- cannot be used on this element 
                |$ks
                |
                |<description>
                |  Include must be used only on classes, functions and properties. All other uses are prohibited.
                |</description>
            """.trimMargin()
            )
        }
        context.originatingFiles.add(ks.requireKSFile())
    }

    companion object {
        @Include(bindTo = BindTarget.Map)
        @Order(10)
        val GraphContributorInclude.asGraphContributor: GraphContributor
            get() = this
    }
}
