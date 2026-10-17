package spike.compiler.generator

import com.squareup.kotlinpoet.KModifier
import spike.compiler.graph.EntryPointVisibility

private val Modifiers = mapOf(
    EntryPointVisibility.Public to KModifier.PUBLIC,
    EntryPointVisibility.Internal to KModifier.INTERNAL,
)

internal val EntryPointVisibility.modifier: KModifier
    get() = Modifiers.getValue(this)
