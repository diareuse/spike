package spike.compiler.generator

import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.TypeName

class LinearReferences(
    private val part: Int,
    private val layout: LinearLayout,
    private val guards: LinearGuards,
) {

    fun reference(step: Int, type: TypeName): CodeBlock = when {
        guards.guardOf(step) != null -> CodeBlock.of("(%L)", castReference(step, type))
        layout.partOf(step) == part -> CodeBlock.of("v%L", step)
        else -> slotReference(step, type)
    }

    /**
     * References a step as the receiver of a member access, parenthesising a cast so the access applies to it.
     */
    fun receiverReference(step: Int, type: TypeName): CodeBlock = when {
        guards.guardOf(step) == null && layout.partOf(step) != part -> CodeBlock.of("(%L)", slotReference(step, type))
        else -> reference(step, type)
    }

    fun castReference(step: Int, type: TypeName): CodeBlock = when (layout.partOf(step)) {
        part -> CodeBlock.of("v%L as %T", step, type)
        else -> slotReference(step, type)
    }

    private fun slotReference(step: Int, type: TypeName) =
        CodeBlock.of("instances[%L] as %T", layout.slotOf(step), type)

}
