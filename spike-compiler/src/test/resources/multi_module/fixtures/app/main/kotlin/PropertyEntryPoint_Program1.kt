@file:Suppress("UNCHECKED_CAST", "unused", "RedundantVisibilityModifier", "ClassName", "USELESS_CAST")

import kotlin.Any
import kotlin.String
import kotlin.Suppress
import spike.Provider
import spike.factory.DependencyId

internal object PropertyEntryPoint_Program1 {
  public fun resolve(factory: PropertyEntryPoint_Factory): Any? = part0(factory)

  private fun part0(factory: PropertyEntryPoint_Factory): Any? = Provider { factory.get<String>(DependencyId(2)) }
}
