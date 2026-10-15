@file:Suppress("UNCHECKED_CAST", "unused", "RedundantVisibilityModifier", "ClassName", "USELESS_CAST")

import kotlin.Any
import kotlin.Suppress
import kotlin.collections.mapOf

internal object PropertyEntryPoint_Program2 {
  public fun resolve(factory: PropertyEntryPoint_Factory): Any? = part0(factory)

  private fun part0(factory: PropertyEntryPoint_Factory): Any? {
    val v0: Cat = redCat()
    return mapOf<CatType, Cat>(
      CatType.Red to v0 as Cat
    )
  }
}
