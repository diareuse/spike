@file:Suppress("UNCHECKED_CAST", "unused", "RedundantVisibilityModifier", "ClassName")

import kotlin.Any
import kotlin.Array
import kotlin.Int
import kotlin.String
import kotlin.Suppress
import kotlin.collections.mapOf

public class PropertyEntryPoint_DependencyHolder0(
  private val factory: PropertyEntryPoint_Factory,
) {
  internal fun create(buffer: Array<Any?>, position: Int): Any = when(position) {
    0 -> mapOf<String, IntWorker>(
      "primary" to buffer[0] as Primary,
      "secondary" to buffer[1] as Secondary
    )
    1 -> Secondary()
    2 -> Primary()
    else -> error("Invalid position")
  }
}
