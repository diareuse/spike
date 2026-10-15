package spike.compiler.processor

import com.google.devtools.ksp.symbol.KSAnnotation
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSDeclaration
import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import com.google.devtools.ksp.symbol.KSType

class IncludeContributorBindAsRemoved(
    private val origin: IncludeContributor,
) : IncludeContributor {
    override fun contribute(context: GraphContext, annotated: KSClassDeclaration) {
        annotated.bindAsTarget()?.let { target ->
            annotated.failOnBindAs("val ${annotated.simpleName.asString()}.as$target: $target get() = this")
        }
        origin.contribute(context, annotated)
    }

    override fun contribute(context: GraphContext, annotated: KSFunctionDeclaration) {
        annotated.bindAsTarget()?.let { target ->
            val parameters = annotated.parameters
                .joinToString { "${it.name?.asString()}: ${it.type.resolve().render()}" }
            annotated.failOnBindAs(
                "fun ${annotated.simpleName.asString()}($parameters): $target = ...",
                "return $target from ${annotated.simpleName.asString()} instead"
            )
        }
        origin.contribute(context, annotated)
    }

    override fun contribute(context: GraphContext, annotated: KSPropertyDeclaration) {
        annotated.bindAsTarget()?.let { target ->
            annotated.failOnBindAs(
                "val ${annotated.simpleName.asString()}: $target get() = ...",
                "declare it as $target"
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

    private fun KSDeclaration.failOnBindAs(
        declaration: String,
        hint: String = "bind it with an @Include property or function",
    ): Nothing {
        val lines = listOf(includeLine()) + carriedAnnotations() + declaration
        error(
            """bindAs was removed from @Include, $hint:
                |
                |<expected>
                |${lines.joinToString("\n") { "  $it" }}
                |</expected>
                |
                |<description>
                |  Any @Include property or function binds its declared type. Move bindTo, @spike.Key and
                |  @spike.Qualifier annotations from ${simpleName.asString()} to the binding.
                |</description>""".trimMargin()
        )
    }

    private fun KSDeclaration.includeLine(): String {
        val bindTo = include()?.arguments?.firstOrNull { it.name?.asString() == "bindTo" }?.value
        val target = (bindTo as? KSClassDeclaration ?: (bindTo as? KSType)?.declaration)?.simpleName?.asString()
        return when (target) {
            null, "None" -> "@spike.Include"
            else -> "@spike.Include(bindTo = spike.BindTarget.$target)"
        }
    }

    private fun KSDeclaration.carriedAnnotations(): List<String> = annotations
        .filter { annotation ->
            annotation.annotationType.resolve().declaration.annotations.any {
                it.annotationType.resolve().declaration.qualifiedName?.asString() in carried
            }
        }
        .map { annotation ->
            val arguments = annotation.arguments.joinToString { it.value.render() }
            "@${annotation.shortName.asString()}" + if (arguments.isEmpty()) "" else "($arguments)"
        }
        .toList()

    private fun Any?.render(): String = when (this) {
        is String -> "\"$this\""
        is KSType -> declaration.simpleName.asString()
        is KSClassDeclaration -> simpleName.asString()
        else -> toString()
    }

    private companion object {
        val carried = setOf("spike.Qualifier", "spike.Key")
    }
}
