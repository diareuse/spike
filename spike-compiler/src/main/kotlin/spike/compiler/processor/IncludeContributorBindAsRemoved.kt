package spike.compiler.processor

import com.google.devtools.ksp.symbol.KSAnnotation
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSDeclaration
import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import com.google.devtools.ksp.symbol.KSType
import spike.compiler.diagnostic.DiagnosticException
import spike.compiler.diagnostic.DiagnosticIncludeBindAs
import spike.compiler.diagnostic.Fix

class IncludeContributorBindAsRemoved(
    private val origin: IncludeContributor,
) : IncludeContributor {
    override fun contribute(context: GraphContext, annotated: KSClassDeclaration) {
        annotated.bindAsTarget()?.let { target ->
            val name = annotated.simpleName.asString()
            val moving = if (annotated.carriedAnnotations().isEmpty() && annotated.bindTo() == null) "" else
                ", moving bindTo, @spike.Key and @spike.Qualifier annotations to it"
            annotated.failOnBindAs(
                "Remove bindAs from $name and bind it with an @Include property$moving",
                "val $name.as$target: $target get() = this"
            )
        }
        origin.contribute(context, annotated)
    }

    override fun contribute(context: GraphContext, annotated: KSFunctionDeclaration) {
        annotated.bindAsTarget()?.let { target ->
            annotated.failOnBindAs(
                "Remove bindAs and return $target from ${annotated.simpleName.asString()}",
                annotated.signature(target)
            )
        }
        origin.contribute(context, annotated)
    }

    override fun contribute(context: GraphContext, annotated: KSPropertyDeclaration) {
        annotated.bindAsTarget()?.let { target ->
            annotated.failOnBindAs(
                "Remove bindAs and declare ${annotated.simpleName.asString()} as $target",
                annotated.signature(target)
            )
        }
        origin.contribute(context, annotated)
    }

    private fun KSDeclaration.include(): KSAnnotation? = annotations.firstOrNull {
        it.annotationType.resolve().declaration.qualifiedName?.asString() == "spike.Include"
    }

    private fun KSDeclaration.bindAsTarget(): String? {
        val bindAs = include()?.arguments?.firstOrNull { it.name?.asString() == "bindAs" }
        val declaration = (bindAs?.value as? KSType)?.declaration
        return declaration?.takeUnless { it.qualifiedName?.asString() == "kotlin.Any" }?.simpleName?.asString()
    }

    private fun KSDeclaration.failOnBindAs(description: String, declaration: String): Nothing {
        val lines = listOf(includeLine()) + carriedAnnotations() + declaration
        val fix = Fix(description, lines.joinToString("\n"))
        throw DiagnosticException(DiagnosticIncludeBindAs(described, fix, asOrigin()))
    }

    private fun KSDeclaration.bindTo(): String? {
        val bindTo = include()?.arguments?.firstOrNull { it.name?.asString() == "bindTo" }?.value
        val target = (bindTo as? KSClassDeclaration ?: (bindTo as? KSType)?.declaration)?.simpleName?.asString()
        return target.takeUnless { it == "None" }
    }

    private fun KSDeclaration.includeLine(): String =
        bindTo()?.let { "@spike.Include(bindTo = spike.BindTarget.$it)" } ?: "@spike.Include"

    private fun KSDeclaration.carriedAnnotations(): List<String> = annotations
        .filter { annotation ->
            annotation.annotationType.resolve().declaration.annotations.any {
                it.annotationType.resolve().declaration.qualifiedName?.asString() in carried
            }
        }
        .map { it.rendered() }
        .toList()

    private companion object {
        val carried = setOf("spike.Qualifier", "spike.Key")
    }
}
