package spike.compiler.processor.symbol

import com.google.devtools.ksp.symbol.KSClassDeclaration
import spike.Include

@Include
class UnresolvedExportImportsConstructor(private val unresolved: UnresolvedTypes) : UnresolvedExportImports {
    override fun of(generated: List<KSClassDeclaration>) = generated.flatMap { export ->
        val types = listOfNotNull(export.primaryConstructor).asSequence()
            .flatMap { constructor -> constructor.parameters.asSequence().map { it.type }.resolved(constructor) }
        unresolved.of(types).map { UnresolvedCompiledType(it.type, export.exportName()) }.distinct()
    }
}

fun KSClassDeclaration.exportName(): String =
    "export ${superTypes.first().resolve().declaration.qualifiedName?.asString()}"
