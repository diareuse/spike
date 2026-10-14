@file:Suppress("UNCHECKED_CAST", "unused", "RedundantVisibilityModifier", "ClassName", "USELESS_CAST")

import kotlin.Any
import kotlin.Suppress
import spike.Provider
import spike.factory.DependencyId

internal object PropertyEntryPoint_Program0 {
  public fun resolve(factory: PropertyEntryPoint_Factory): Any? = part0(factory)

  private fun part0(factory: PropertyEntryPoint_Factory): Any? {
    val v0: Provider<Food> = Provider { factory.get<Food>(DependencyId(1)) }
    return factory.singletons.getOrPut(0) {
        Cat(v0)
    }
  }
}
