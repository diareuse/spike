package spike.compiler.processor.symbol

import com.google.devtools.ksp.KspExperimental
import com.google.devtools.ksp.getDeclaredFunctions
import com.google.devtools.ksp.isAbstract
import com.google.devtools.ksp.isAnnotationPresent
import com.google.devtools.ksp.symbol.ClassKind
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSDeclaration
import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import com.google.devtools.ksp.symbol.KSNode
import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import com.google.devtools.ksp.symbol.KSType
import com.google.devtools.ksp.symbol.KSTypeReference
import com.google.devtools.ksp.visitor.KSDefaultVisitor
import spike.EntryPoint
import spike.Include
import spike.compiler.processor.includedConstructorOrNull

@Include
@OptIn(KspExperimental::class)
class SignatureTypesDeclaration :
    KSDefaultVisitor<Unit, Sequence<SignatureType>>(enableNewFeatures = true),
    SignatureTypes {
    override fun of(symbol: KSAnnotated): Sequence<SignatureType> = symbol.accept(this, Unit)

    override fun defaultHandler(node: KSNode, data: Unit) = emptySequence<SignatureType>()

    override fun visitClassDeclaration(classDeclaration: KSClassDeclaration, data: Unit) = classDeclaration.types()

    override fun visitFunctionDeclaration(function: KSFunctionDeclaration, data: Unit) = function.types()

    override fun visitPropertyDeclaration(property: KSPropertyDeclaration, data: Unit) = property.types()

    private fun KSClassDeclaration.types(): Sequence<SignatureType> =
        annotated(this) + superTypes.resolved(this) +
            listOfNotNull(includedConstructorOrNull()).flatMap { it.types() } +
            abstractMembers() +
            factories().flatMap { factory -> factory.getDeclaredFunctions().flatMap { it.types() } }

    private fun KSClassDeclaration.abstractMembers(): Sequence<SignatureType> {
        if (classKind != ClassKind.INTERFACE) return emptySequence()
        return getAllProperties().filter { it.isAbstract() }.flatMap { it.types() } +
            getAllFunctions().filter { it.isAbstract }.flatMap { it.types() }
    }

    private fun KSClassDeclaration.factories() = declarations
        .filterIsInstance<KSClassDeclaration>()
        .filter { it.isAnnotationPresent(EntryPoint.Factory::class) }

    private fun KSFunctionDeclaration.types(): Sequence<SignatureType> =
        annotated(this) + listOfNotNull(extensionReceiver, returnType).asSequence().resolved(this) +
            parameters.asSequence().flatMap { it.annotated(this) + sequenceOf(it.type).resolved(this) }

    private fun KSPropertyDeclaration.types(): Sequence<SignatureType> =
        annotated(this) + listOfNotNull(extensionReceiver, type).asSequence().resolved(this)

    private fun KSAnnotated.annotated(owner: KSDeclaration): Sequence<SignatureType> =
        annotations.flatMap { annotation ->
            sequenceOf(annotation.annotationType).resolved(owner) + annotation.arguments.asSequence()
                .mapNotNull { it.value as? KSType }
                .map { SignatureType(annotation, it, owner) }
        }
}
