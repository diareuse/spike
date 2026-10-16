@file:Suppress("UNCHECKED_CAST", "unused", "RedundantVisibilityModifier", "ClassName", "USELESS_CAST")

import kotlin.Any
import kotlin.BooleanArray
import kotlin.String
import kotlin.Suppress
import spike.Provider
import spike.factory.DependencyId

internal object PropertyEntryPoint_Program0 {
  private const val guards: String = "\u0002\u0002\u0000\u0001"

  public fun resolve(factory: PropertyEntryPoint_Factory): Any? {
    val needed = factory._singletons.needed(guards)
    return part0(factory, needed)
  }

  private fun part0(factory: PropertyEntryPoint_Factory, needed: BooleanArray): Any? {
    val v0: Provider<Food>? = if (!needed[0]) null else Provider { factory.get<Food>(DependencyId(1)) }
    return factory._singletons.getOrPut(0) {
        Cat((v0 as Provider<Food>))
    }
  }
}
