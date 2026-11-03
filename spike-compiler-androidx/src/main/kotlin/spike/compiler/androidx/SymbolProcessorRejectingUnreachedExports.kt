package spike.compiler.androidx

import com.google.devtools.ksp.KspExperimental
import com.google.devtools.ksp.getDeclaredFunctions
import com.google.devtools.ksp.getDeclaredProperties
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import spike.compiler.conversion.ConversionProblem
import spike.compiler.conversion.ProblemReporter

@OptIn(KspExperimental::class)
class SymbolProcessorRejectingUnreachedExports(
    private val entryPoint: String,
    private val markers: EntryPointMarkers,
    private val reporter: ProblemReporter,
) : SymbolProcessor {
    override fun process(resolver: Resolver): List<KSAnnotated> {
        val compiled = resolver.getClassDeclarationByName(resolver.getKSNameFromString(entryPoint))
            ?: return emptyList()
        val factory = resolver.getClassDeclarationByName(resolver.getKSNameFromString("${entryPoint}_Factory"))
        val reached = factory?.getDeclaredProperties()
            ?.mapNotNull { it.type.resolve().declaration.qualifiedName?.asString() }
            ?.toSet()
            .orEmpty()
        val unreached = resolver.getDeclarationsFromPackage(GeneratedPackage)
            .filterIsInstance<KSClassDeclaration>()
            .filter(::contributesViewModels)
            .filter { it.qualifiedName?.asString() !in reached }
        val module = markers.module(resolver)
        for (export in unreached) reporter.report(problem(compiled, module, exportOf(export)))
        return emptyList()
    }

    private fun contributesViewModels(generated: KSClassDeclaration) = generated.getDeclaredFunctions().any { function ->
        function.annotations.any { it.shortName.asString() == ViewModelNames.ViewModelKey.simpleName }
    }

    private fun exportOf(generated: KSClassDeclaration): String =
        generated.superTypes.firstOrNull()?.resolve()?.declaration?.qualifiedName?.asString()
            ?: generated.qualifiedName?.asString().orEmpty()

    private fun problem(compiled: KSClassDeclaration, module: String, export: String) = ConversionProblem(
        "SPIKE-VIEWMODEL-ENTRY-POINT-INCOMPLETE",
        "$entryPoint is compiled in $module, which does not see $export, so the ViewModels exported by $export " +
            "are missing from it. If $module is a library, mark it as one: spike { library = true } with the " +
            "io.github.diareuse.spike Gradle plugin, the KSP option spike.library=true, or an @Export interface " +
            "in it. If $module is this module's main source set (a test compilation), add the dependency that " +
            "declares $export to the main source set too.",
        compiled,
    )

    private companion object {
        const val GeneratedPackage = "spike.generated"
    }
}
