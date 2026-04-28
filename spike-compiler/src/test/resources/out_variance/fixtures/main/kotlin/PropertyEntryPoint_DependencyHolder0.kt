@file:Suppress("UNCHECKED_CAST", "unused", "RedundantVisibilityModifier", "ClassName")

import kotlin.Any
import kotlin.Array
import kotlin.Int
import kotlin.Suppress
import kotlin.collections.mapOf
import kotlin.reflect.KClass

public class PropertyEntryPoint_DependencyHolder0(
  private val factory: PropertyEntryPoint_Factory,
) {
  internal fun create(buffer: Array<Any?>, position: Int): Any = when(position) {
    0 -> mapOf<KClass<out Cat>, Cat>(
      SomeCat::class to buffer[0] as SomeCat
    )
    1 -> SomeCat()
    else -> error("Invalid position")
  }
}
