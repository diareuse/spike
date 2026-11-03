package spike.compiler.processor

import spike.compiler.graph.ExportedUse
import spike.compiler.graph.Type
import spike.compiler.graph.displayName

class CompiledUse(val use: ExportedUse, val exportedBy: Type) {
    val description get() = "${use.binding} of @Export ${exportedBy.displayName}"

    fun isOf(type: Type) = use.type == type.toString()

    fun uses(type: String) = type in use.uses
}
