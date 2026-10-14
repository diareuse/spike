@file:Suppress("UNCHECKED_CAST", "unused", "RedundantVisibilityModifier", "ClassName", "USELESS_CAST")

import kotlin.Any
import kotlin.String
import kotlin.Suppress
import kotlin.collections.mapOf

internal object PropertyEntryPoint_Program0 {
  public fun resolve(factory: PropertyEntryPoint_Factory): Any? = part0(factory)

  private fun part0(factory: PropertyEntryPoint_Factory): Any? {
    val v0: Cat = ragamuffin()
    val v1: Cat = sphynx()
    return mapOf<String, Cat>(
      "sphynx" to v1 as Cat,
      "ragamuffin" to v0 as Cat
    )
  }
}
