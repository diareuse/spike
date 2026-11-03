package spike.compiler.androidx

import com.google.devtools.ksp.KspExperimental
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.symbol.KSClassDeclaration

@OptIn(KspExperimental::class)
class EntryPointMarkers(private val packageName: String) {
    fun compiled(resolver: Resolver): List<KSClassDeclaration> = resolver.getDeclarationsFromPackage(packageName)
        .filterIsInstance<KSClassDeclaration>()
        .filter { it.simpleName.asString().startsWith(ViewModelNames.EntryPointMarkerPrefix) }
        .filter { it.containingFile == null }
        .toList()

    fun modules(resolver: Resolver): List<String> =
        compiled(resolver).map { it.simpleName.asString().removePrefix(ViewModelNames.EntryPointMarkerPrefix) }

    fun module(resolver: Resolver): CompiledModule = CompiledModule(modules(resolver).firstOrNull())
}
