@file:Suppress("UNCHECKED_CAST", "unused", "RedundantVisibilityModifier", "ClassName", "USELESS_CAST")

import kotlin.Any
import kotlin.String
import kotlin.Suppress
import kotlin.collections.mapOf

internal object PropertyEntryPoint_Program0 {
  public fun resolve(factory: PropertyEntryPoint_Factory): Any? = part0(factory)

  private fun part0(factory: PropertyEntryPoint_Factory): Any? {
    val v0: Secondary = Secondary()
    val v1: Primary = Primary()
    return mapOf<String, IntWorker>(
      "primary" to v1 as IntWorker,
      "secondary" to v0 as IntWorker
    )
  }
}
