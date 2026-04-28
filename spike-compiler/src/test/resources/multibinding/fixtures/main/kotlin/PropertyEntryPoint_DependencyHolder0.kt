@file:Suppress("UNCHECKED_CAST", "unused", "RedundantVisibilityModifier", "ClassName")

import kotlin.Any
import kotlin.Array
import kotlin.Int
import kotlin.String
import kotlin.Suppress
import kotlin.collections.listOf
import kotlin.collections.mapOf
import kotlin.collections.setOf

public class PropertyEntryPoint_DependencyHolder0(
  private val factory: PropertyEntryPoint_Factory,
) {
  internal fun create(buffer: Array<Any?>, position: Int): Any = when(position) {
    0 -> mapOf<String, Cat>(
      "sphynx" to buffer[0] as Cat,
      "ragamuffin" to buffer[1] as Cat
    )
    1 -> ragamuffin()
    2 -> sphynx()
    3 -> listOf(buffer[0] as Cat, buffer[1] as Cat)
    4 -> bombay()
    5 -> toyger()
    6 -> setOf(buffer[0] as Cat, buffer[1] as Cat)
    7 -> abyssinian()
    8 -> ragdoll()
    else -> error("Invalid position")
  }
}
