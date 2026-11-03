package spike.compiler.processor

import com.google.devtools.ksp.symbol.KSClassDeclaration
import spike.Include
import spike.compiler.graph.ExportedUse

@Include
class CompiledUsesReader {
    fun of(generated: List<KSClassDeclaration>): List<CompiledUse> = generated.flatMap { klass ->
        val exportedBy = klass.superTypes.first().resolve().toType()
        val entries = klass.annotations
            .firstOrNull { it.shortName.asString() == "ExportedUses" }
            ?.arguments?.single()?.value as? List<*>
        entries.orEmpty().filterIsInstance<String>().map { CompiledUse(ExportedUse.decoded(it), exportedBy) }
    }
}
