package spike.compiler.processor

import com.google.devtools.ksp.KspExperimental
import com.google.devtools.ksp.getConstructors
import com.google.devtools.ksp.isAnnotationPresent
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.symbol.KSClassDeclaration
import spike.Include
import spike.Inject
import spike.Singleton

@OptIn(KspExperimental::class)
class GraphContributorIncludeClass(
    private val bindAs: IncludeBindAsContributor
) : GraphContributor {
    override fun contribute(context: GraphContext, resolver: Resolver) {
        val classes = resolver
            .getSymbolsWithAnnotation(Include::class.qualifiedName!!)
            .filterIsInstance<KSClassDeclaration>()
        for (cls in classes) {
            bindAs.contribute(context, cls)
            val constructors = cls.getConstructors().toList()
            val constructor = when {
                constructors.size > 1 -> checkNotNull(constructors.firstOrNull { it.isAnnotationPresent(Inject::class) }) {
                    "Include class must have a constructor annotated with @spike.Inject if it has more than one constructor"
                }

                else -> constructors.single()
            }
            context.builder.addConstructor(
                type = cls.toType().qualifiedBy(cls.findQualifiers()),
                invocation = constructor.toInvocation(),
                singleton = cls.isAnnotationPresent(Singleton::class)
            )
        }
    }
}
