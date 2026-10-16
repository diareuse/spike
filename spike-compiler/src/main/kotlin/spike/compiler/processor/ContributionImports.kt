package spike.compiler.processor

import com.google.devtools.ksp.symbol.KSClassDeclaration
import spike.BindTarget
import spike.compiler.graph.Key
import spike.compiler.graph.Type
import spike.compiler.graph.TypeFactory

/**
 * Finds the multibinding contributions that @Export modules publish through their generated classes.
 */
fun interface ContributionImports {
    fun of(generated: List<KSClassDeclaration>): List<ContributionImport>
}

class ContributionImport(
    val target: BindTarget,
    val key: Key?,
    val external: TypeFactory.External,
    val exportedBy: Type,
)
