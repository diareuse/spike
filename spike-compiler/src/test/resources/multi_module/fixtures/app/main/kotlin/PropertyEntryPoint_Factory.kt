@file:Suppress("ClassName", "RedundantVisibilityModifier")

import kotlin.String
import kotlin.Suppress
import spike.Provider
import spike.factory.DependencyId
import spike.factory.SingletonHolder
import spike.factory.SingletonOwner
import spike.generated.ExportedImpl

public class PropertyEntryPoint_Factory(
  singletons: SingletonHolder? = null,
) : SingletonOwner(singletons),
    PropertyEntryPoint {
  public val exportedImpl: ExportedImpl =
      ExportedImpl(kotlinString = get(DependencyId(1)), singletons = _singletons.scope("spike.generated.ExportedImpl"))

  override val lib: Lib
    get() = get(DependencyId(0))

  public val kotlinString: Provider<String>
    get() = get(DependencyId(1))

  @Suppress("UNCHECKED_CAST")
  public fun <T> `get`(id: DependencyId): T = when (id.id) {
    0 -> PropertyEntryPoint_Program0.resolve(this)
    1 -> PropertyEntryPoint_Program1.resolve(this)
    2 -> PropertyEntryPoint_Program2.resolve(this)
    else -> error("Invalid identifier $id")
  } as T
}
