package spike.compiler.processor

import com.google.devtools.ksp.KspExperimental
import com.google.devtools.ksp.getConstructors
import com.google.devtools.ksp.isAnnotationPresent
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import spike.Include
import spike.compiler.graph.Key
import spike.compiler.graph.Type
import kotlin.reflect.KClass

@Include
class IncludeContributorViewModel : IncludeContributor {
    @OptIn(KspExperimental::class)
    override fun contribute(
        context: GraphContext,
        annotated: KSClassDeclaration,
    ) {
        val constructors = annotated.getConstructors().toList()
        val constructor = when {
            constructors.size > 1 -> checkNotNull(constructors.firstOrNull { it.isAnnotationPresent(Include.Constructor::class) }) {
                "Include class (${annotated.qualifiedName?.asString()}) must have a constructor " +
                        "annotated with @spike.Include.Constructor if it has more than one constructor"
            }
            else -> constructors.single()
        }
        val type = annotated.toType(false).qualifiedBy(annotated.findQualifiers())
        val keyType = Type.Parametrized(KClass::class.toType(), listOf(Type.WithVariance(Type.Simple("androidx.lifecycle", "ViewModel", false), Type.WithVariance.Variance.OUT)), false)
        val key = Key(keyType, type)
        val commonType = Type.Simple("androidx.lifecycle", "ViewModel", false)
        context.multibind.addToMap(commonType, key) {
            addConstructor(
                type = annotated.toType(false).qualifiedBy(annotated.findQualifiers()),
                invocation = constructor.toInvocation(),
                singleton = false,
            )
        }
    }

    override fun contribute(
        context: GraphContext,
        annotated: KSFunctionDeclaration,
    ) {
        error("ViewModels must not be included as a function")
    }
}
