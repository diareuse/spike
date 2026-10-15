package spike.compiler.processor

import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import spike.Include
import spike.compiler.graph.Key
import spike.compiler.graph.Type
import kotlin.reflect.KClass

@Include
class IncludeContributorViewModel : IncludeContributor {
    override fun contribute(
        context: GraphContext,
        annotated: KSClassDeclaration,
    ) {
        val constructor = annotated.includedConstructor()
        val type = annotated.toType(false).qualifiedBy(annotated.findQualifiers())
        val keyType = Type.Parametrized(
            envelope = KClass::class.toType(),
            typeArguments = Type.Simple("androidx.lifecycle", "ViewModel", false)
                .let { Type.WithVariance(it, Type.WithVariance.Variance.OUT) }
                .let(::listOf),
            nullable = false
        )
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

    override fun contribute(
        context: GraphContext,
        annotated: KSPropertyDeclaration,
    ) {
        error("ViewModels must not be included as a property")
    }
}
