@file:Suppress("UNCHECKED_CAST", "unused", "RedundantVisibilityModifier", "ClassName", "USELESS_CAST")

import kotlin.Any
import kotlin.Suppress
import kotlin.collections.listOf

internal object PropertyEntryPoint_Program1 {
  public fun resolve(factory: PropertyEntryPoint_Factory): Any? = part0(factory)

  private fun part0(factory: PropertyEntryPoint_Factory): Any? {
    val v0: Cat = bombay()
    val v1: Cat = toyger()
    return listOf<Cat>(v1, v0)
  }
}
