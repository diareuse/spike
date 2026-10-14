@file:Suppress("UNCHECKED_CAST", "unused", "RedundantVisibilityModifier", "ClassName", "USELESS_CAST")

import kotlin.Any
import kotlin.Suppress

internal object PropertyEntryPoint_Program0 {
  public fun resolve(factory: PropertyEntryPoint_Factory): Any? = part0(factory)

  private fun part0(factory: PropertyEntryPoint_Factory): Any? {
    val v0: Cat = Cat()
    val v1: Box = Box(v0)
    val v2: TeleportedCat = TeleportedCat(v1, v0)
    return ComplexObject(v0, v1, v2)
  }
}
