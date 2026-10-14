@file:Suppress("UNCHECKED_CAST", "unused", "RedundantVisibilityModifier", "ClassName", "USELESS_CAST")

import kotlin.Any
import kotlin.Suppress
import kotlin.lazy
import spike.factory.DependencyId

internal object PropertyEntryPoint_Program3 {
  public fun resolve(factory: PropertyEntryPoint_Factory): Any? = part0(factory)

  private fun part0(factory: PropertyEntryPoint_Factory): Any? = lazy { factory.get<Cat>(DependencyId(0)) }
}
