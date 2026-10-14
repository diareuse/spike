@file:Suppress("UNCHECKED_CAST", "unused", "RedundantVisibilityModifier", "ClassName", "USELESS_CAST")

import kotlin.Any
import kotlin.Suppress
import kotlin.collections.setOf

internal object PropertyEntryPoint_Program6 {
  public fun resolve(factory: PropertyEntryPoint_Factory): Any? = part0(factory)

  private fun part0(factory: PropertyEntryPoint_Factory): Any? {
    val v0: Cat = abyssinian()
    val v1: Cat = ragdoll()
    return setOf<Cat>(v1, v0)
  }
}
