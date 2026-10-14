package spike.compiler.generator

import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.TypeName

class LinearReferences(
    private val part: Int,
    private val layout: LinearLayout,
) {

    fun reference(step: Int, type: TypeName): CodeBlock = when (layout.partOf(step)) {
        part -> CodeBlock.of("v%L", step)
        else -> slotReference(step, type)
    }

    fun castReference(step: Int, type: TypeName): CodeBlock = when (layout.partOf(step)) {
        part -> CodeBlock.of("v%L as %T", step, type)
        else -> slotReference(step, type)
    }

    private fun slotReference(step: Int, type: TypeName) =
        CodeBlock.of("instances[%L] as %T", layout.slotOf(step), type)

}
