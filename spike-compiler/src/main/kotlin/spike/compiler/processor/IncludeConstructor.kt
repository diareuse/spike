package spike.compiler.processor

import com.google.devtools.ksp.KspExperimental
import com.google.devtools.ksp.getConstructors
import com.google.devtools.ksp.isAnnotationPresent
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import spike.Include
import spike.compiler.diagnostic.DiagnosticException
import spike.compiler.diagnostic.DiagnosticIncludeConstructorAmbiguous
import spike.compiler.diagnostic.DiagnosticIncludeNoConstructor
import spike.compiler.diagnostic.Involved

@OptIn(KspExperimental::class)
fun KSClassDeclaration.includedConstructorOrNull(): KSFunctionDeclaration? {
    val constructors = getConstructors().toList()
    return constructors.singleOrNull()
        ?: constructors.firstOrNull { it.isAnnotationPresent(Include.Constructor::class) }
}

fun KSClassDeclaration.includedConstructor(): KSFunctionDeclaration {
    includedConstructorOrNull()?.let { return it }
    val constructors = getConstructors().toList()
    val name = simpleName.asString()
    if (constructors.isEmpty()) {
        throw DiagnosticException(DiagnosticIncludeNoConstructor(classKind.type, name, asOrigin()))
    }
    throw DiagnosticException(
        DiagnosticIncludeConstructorAmbiguous(
            name = name,
            constructors = constructors.map {
                Involved("constructor $name(${it.parameters.rendered()})", it.asOrigin())
            },
            suggested = "constructor(${constructors.first().parameters.rendered()})",
        )
    )
}
