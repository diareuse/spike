@file:Suppress("UNCHECKED_CAST", "unused", "RedundantVisibilityModifier", "ClassName", "USELESS_CAST")

import kotlin.Any
import kotlin.Suppress
import kotlin.collections.mapOf
import kotlin.reflect.KClass

internal object PropertyEntryPoint_Program0 {
  public fun resolve(factory: PropertyEntryPoint_Factory): Any? = part0(factory)

  private fun part0(factory: PropertyEntryPoint_Factory): Any? {
    val v0: SomeCat = SomeCat()
    return mapOf<KClass<out Cat>, Cat>(
      SomeCat::class to v0 as Cat
    )
  }
}
