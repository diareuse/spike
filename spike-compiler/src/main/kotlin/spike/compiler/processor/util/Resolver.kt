package spike.compiler.processor.util

import com.google.devtools.ksp.KspExperimental
import com.google.devtools.ksp.isAnnotationPresent
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSDeclaration

@KspExperimental
inline fun <reified T: Annotation> Resolver.getSymbolsWithAnnotation(packageName: String): Sequence<KSAnnotated> {
    return getDeclarationsFromPackage(packageName).filter { it.isAnnotationPresent(T::class) }
}

@KspExperimental
inline fun <reified T: Annotation> Resolver.getSymbolsWithAnnotation(): Sequence<KSAnnotated> {
    return getSymbolsWithAnnotation(T::class.qualifiedName!!)
}