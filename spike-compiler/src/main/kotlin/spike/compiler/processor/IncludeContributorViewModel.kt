package spike.compiler.processor

import com.google.devtools.ksp.KspExperimental
import com.google.devtools.ksp.getConstructors
import com.google.devtools.ksp.isAnnotationPresent
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import spike.Inject
import spike.graph.Key
import spike.graph.Type
import kotlin.reflect.KClass

class IncludeContributorViewModel : IncludeContributor {
    @OptIn(KspExperimental::class)
    override fun contribute(
        context: GraphContext,
        annotated: KSClassDeclaration
    ) {
        val constructors = annotated.getConstructors().toList()
        val constructor = when {
            constructors.size > 1 -> checkNotNull(constructors.firstOrNull { it.isAnnotationPresent(Inject::class) }) {
                "Include class (${annotated.qualifiedName?.asString()}) must have a constructor annotated with @spike.Inject if it has more than one constructor"
            }

            else -> constructors.single()
        }
        val type = annotated.toType().qualifiedBy(annotated.findQualifiers())
        val keyType = Type.Parametrized(KClass::class.toType(), listOf(Type.WithVariance(Type.Simple("androidx.lifecycle", "ViewModel"), Type.WithVariance.Variance.OUT)))
        val key = Key(keyType, type)
        val commonType = Type.Simple("androidx.lifecycle", "ViewModel")
        context.multibind.addToMap(commonType, key) {
            addConstructor(
                type = annotated.toType().qualifiedBy(annotated.findQualifiers()),
                invocation = constructor.toInvocation(),
                singleton = false
            )
        }
    }

    override fun contribute(
        context: GraphContext,
        annotated: KSFunctionDeclaration
    ) {
        error("ViewModels must not be included as a function")
    }
}