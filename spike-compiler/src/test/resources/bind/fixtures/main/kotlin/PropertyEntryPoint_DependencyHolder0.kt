@file:Suppress("UNCHECKED_CAST", "unused", "RedundantVisibilityModifier", "ClassName")

import kotlin.Any
import kotlin.Array
import kotlin.Int
import kotlin.Suppress

public class PropertyEntryPoint_DependencyHolder0(
  private val factory: PropertyEntryPoint_Factory,
) {
  internal fun create(buffer: Array<Any?>, position: Int): Any = when(position) {
    0 -> buffer[0] as Cat
    1 -> Sphynx()
    else -> error("Invalid position")
  }
}
