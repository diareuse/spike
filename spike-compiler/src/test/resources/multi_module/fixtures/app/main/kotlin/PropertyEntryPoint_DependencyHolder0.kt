@file:Suppress("UNCHECKED_CAST", "unused", "RedundantVisibilityModifier", "ClassName")

import kotlin.Any
import kotlin.Array
import kotlin.Int
import kotlin.String
import kotlin.Suppress
import spike.Provider
import spike.factory.DependencyId

public class PropertyEntryPoint_DependencyHolder0(
  private val factory: PropertyEntryPoint_Factory,
) {
  internal fun create(buffer: Array<Any?>, position: Int): Any = when(position) {
    0 -> factory.exportedImpl.lib
    1 -> Provider { factory.get<String>(DependencyId(2)) }
    2 -> prefix()
    else -> error("Invalid position")
  }
}
