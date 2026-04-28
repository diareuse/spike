@file:Suppress("UNCHECKED_CAST", "unused", "RedundantVisibilityModifier", "ClassName")

import kotlin.Any
import kotlin.Array
import kotlin.Int
import kotlin.Suppress

public class PropertyEntryPoint_DependencyHolder0(
  private val factory: PropertyEntryPoint_Factory,
) {
  internal fun create(buffer: Array<Any?>, position: Int): Any = when(position) {
    0 -> ComplexObject(buffer[0] as Cat, buffer[1] as Box, buffer[2] as TeleportedCat)
    1 -> Cat()
    2 -> Box(buffer[0] as Cat)
    3 -> TeleportedCat(buffer[0] as Box, buffer[1] as Cat)
    else -> error("Invalid position")
  }
}
