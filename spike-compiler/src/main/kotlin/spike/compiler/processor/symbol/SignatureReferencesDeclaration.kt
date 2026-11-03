package spike.compiler.processor.symbol

import com.google.devtools.ksp.KspExperimental
import com.google.devtools.ksp.getDeclaredFunctions
import com.google.devtools.ksp.isAbstract
import com.google.devtools.ksp.isAnnotationPresent
import com.google.devtools.ksp.symbol.ClassKind
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import com.google.devtools.ksp.symbol.KSTypeReference
import spike.EntryPoint
import spike.Include
import spike.compiler.processor.includedConstructorOrNull

@Include
@OptIn(KspExperimental::class)
class SignatureReferencesDeclaration : SignatureReferences {
    override fun of(symbol: KSAnnotated): Sequence<KSTypeReference> = when (symbol) {
        is KSClassDeclaration -> symbol.references()
        is KSFunctionDeclaration -> symbol.references()
        is KSPropertyDeclaration -> symbol.references()
        else -> emptySequence()
    }

    private fun KSClassDeclaration.references(): Sequence<KSTypeReference> =
        annotationTypes() + superTypes +
            listOfNotNull(includedConstructorOrNull()).flatMap { it.references() } +
            abstractMembers() +
            factories().flatMap { factory -> factory.getDeclaredFunctions().flatMap { it.references() } }

    private fun KSClassDeclaration.abstractMembers(): Sequence<KSTypeReference> {
        if (classKind != ClassKind.INTERFACE) return emptySequence()
        return getAllProperties().filter { it.isAbstract() }.flatMap { it.references() } +
            getAllFunctions().filter { it.isAbstract }.flatMap { it.references() }
    }

    private fun KSClassDeclaration.factories() = declarations
        .filterIsInstance<KSClassDeclaration>()
        .filter { it.isAnnotationPresent(EntryPoint.Factory::class) }

    private fun KSFunctionDeclaration.references(): Sequence<KSTypeReference> =
        annotationTypes() + listOfNotNull(extensionReceiver, returnType) +
            parameters.asSequence().flatMap { it.annotationTypes() + it.type }

    private fun KSPropertyDeclaration.references(): Sequence<KSTypeReference> =
        annotationTypes() + listOfNotNull(extensionReceiver, type)

    private fun KSAnnotated.annotationTypes() = annotations.map { it.annotationType }
}
