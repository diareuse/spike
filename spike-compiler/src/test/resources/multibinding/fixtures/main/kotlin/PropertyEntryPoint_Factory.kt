@file:Suppress("ClassName", "RedundantVisibilityModifier")

import kotlin.Suppress
import spike.factory.DependencyId
import spike.factory.SingletonHolder

public class PropertyEntryPoint_Factory(
  public val singletons: SingletonHolder = SingletonHolder(),
) {
  @Suppress("UNCHECKED_CAST")
  public fun <T> `get`(id: DependencyId): T = when (id.id) {
    0 -> PropertyEntryPoint_Program0.resolve(this)
    2 -> PropertyEntryPoint_Program1.resolve(this)
    1 -> PropertyEntryPoint_Program2.resolve(this)
    3 -> PropertyEntryPoint_Program3.resolve(this)
    5 -> PropertyEntryPoint_Program4.resolve(this)
    4 -> PropertyEntryPoint_Program5.resolve(this)
    6 -> PropertyEntryPoint_Program6.resolve(this)
    8 -> PropertyEntryPoint_Program7.resolve(this)
    7 -> PropertyEntryPoint_Program8.resolve(this)
    else -> error("Invalid identifier $id")
  } as T
}
