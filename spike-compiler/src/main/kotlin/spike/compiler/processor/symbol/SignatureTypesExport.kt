package spike.compiler.processor.symbol

import com.google.devtools.ksp.getDeclaredFunctions
import com.google.devtools.ksp.getDeclaredProperties
import com.google.devtools.ksp.isConstructor
import com.google.devtools.ksp.isPublic
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSDeclaration
import com.google.devtools.ksp.symbol.KSTypeReference
import spike.Include

@Include
class SignatureTypesExport {
    fun of(export: KSClassDeclaration): Sequence<SignatureType> =
        export.superTypes.resolved(export) + export.imports() + export.functions() + export.properties()

    private fun KSClassDeclaration.imports() = listOfNotNull(primaryConstructor).asSequence()
        .flatMap { constructor -> constructor.parameters.asSequence().map { it.type }.resolved(constructor) }

    private fun KSClassDeclaration.functions() = getDeclaredFunctions()
        .filter { it.isPublic() && !it.isConstructor() }
        .flatMap { function -> listOfNotNull(function.returnType).asSequence().resolved(function) }

    private fun KSClassDeclaration.properties() = getDeclaredProperties()
        .filter { it.isPublic() }
        .flatMap { property -> sequenceOf(property.type).resolved(property) }

    private fun Sequence<KSTypeReference>.resolved(owner: KSDeclaration) =
        map { SignatureType(it, it.resolve(), owner) }
}
