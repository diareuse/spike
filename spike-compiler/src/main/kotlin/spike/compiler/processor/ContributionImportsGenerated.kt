package spike.compiler.processor

import com.google.devtools.ksp.KspExperimental
import com.google.devtools.ksp.getDeclaredFunctions
import com.google.devtools.ksp.isAnnotationPresent
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import spike.Key
import spike.compiler.graph.ExportedContribution
import spike.compiler.graph.TypeFactory

@OptIn(KspExperimental::class)
class ContributionImportsGenerated : ContributionImports {
    override fun of(generated: List<KSClassDeclaration>) = generated.flatMap { klass ->
        val exportedBy = klass.superTypes.first().resolve().toType()
        klass.getDeclaredFunctions().mapNotNull { function ->
            val name = function.simpleName.asString()
            val target = ExportedContribution.targetOf(name) ?: return@mapNotNull null
            ContributionImport(
                target = target,
                key = if (function.isKeyed()) function.findKey() else null,
                external = TypeFactory.External(
                    type = function.returnType!!.toQualifiedType(function),
                    name = name,
                    isMethod = true,
                    origin = klass.toType(false),
                ),
                exportedBy = exportedBy,
            )
        }.toList()
    }

    private fun KSFunctionDeclaration.isKeyed() =
        annotations.any { it.annotationType.resolve().declaration.isAnnotationPresent(Key::class) }
}
