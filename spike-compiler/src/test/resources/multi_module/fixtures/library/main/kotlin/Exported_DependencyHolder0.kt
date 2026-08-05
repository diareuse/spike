@file:Suppress("UNCHECKED_CAST", "unused", "RedundantVisibilityModifier", "ClassName")

import kotlin.Any
import kotlin.Array
import kotlin.Int
import kotlin.String
import kotlin.Suppress

public class Exported_DependencyHolder0(
  private val factory: Exported_Factory,
) {
  internal fun create(buffer: Array<Any?>, position: Int): Any = when(position) {
    0 -> Lib(buffer[0] as String)
    1 -> factory.string
    else -> error("Invalid position")
  }
}
