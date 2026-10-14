@file:Suppress("ClassName", "RedundantVisibilityModifier")

import kotlin.Lazy
import kotlin.Suppress
import spike.Provider
import spike.factory.DependencyId
import spike.factory.SingletonHolder
import spike.factory.SingletonOwner

public class PropertyEntryPoint_Factory(
  singletons: SingletonHolder? = null,
) : SingletonOwner(singletons),
    PropertyEntryPoint {
  override val cat: Cat
    get() = get(DependencyId(0))

  override val cat2: Provider<Cat>
    get() = get(DependencyId(2))

  override val cat3: Lazy<Cat>
    get() = get(DependencyId(3))

  @Suppress("UNCHECKED_CAST")
  public fun <T> `get`(id: DependencyId): T = when (id.id) {
    0 -> PropertyEntryPoint_Program0.resolve(this)
    1 -> PropertyEntryPoint_Program1.resolve(this)
    2 -> PropertyEntryPoint_Program2.resolve(this)
    3 -> PropertyEntryPoint_Program3.resolve(this)
    else -> error("Invalid identifier $id")
  } as T
}
