package spike.compiler.processor

import com.google.devtools.ksp.KspExperimental
import com.google.devtools.ksp.isAnnotationPresent
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import spike.Include
import spike.Singleton
import spike.graph.Member

class GraphContributorIncludeFunction(
    private val bindAs: IncludeBindAsContributor
) : GraphContributor {
    @OptIn(KspExperimental::class)
    override fun contribute(context: GraphContext, resolver: Resolver) {
        val functions = resolver
            .getSymbolsWithAnnotation(Include::class.qualifiedName!!)
            .filterIsInstance<KSFunctionDeclaration>()
        for (func in functions) {
            bindAs.contribute(context, func)
            val qualifiers = func.findQualifiers()
            val returnType = func.returnType!!.toType().qualifiedBy(qualifiers)
            val parentType = func.parentDeclaration?.toType()?.qualifiedBy(qualifiers)
            context.builder.addFactory(
                type = returnType,
                member = Member.Method(
                    packageName = func.packageName.asString(),
                    name = func.simpleName.asString(),
                    returns = returnType,
                    parent = parentType
                ),
                invocation = func.toInvocation(),
                singleton = func.isAnnotationPresent(Singleton::class)
            )
        }
    }
}