@file:Suppress("ClassName", "RedundantVisibilityModifier")

import kotlin.Suppress
import spike.factory.DependencyId
import spike.factory.SingletonHolder
import spike.generated.ExportedImpl

public class PropertyEntryPoint_Factory(
  public val singletons: SingletonHolder = SingletonHolder(),
) {
  public val exportedImpl: ExportedImpl =
      ExportedImpl(kotlinString = get(DependencyId(1)), singletons = singletons.scope("spike.generated.ExportedImpl"))

  @Suppress("UNCHECKED_CAST")
  public fun <T> `get`(id: DependencyId): T = when (id.id) {
    0 -> PropertyEntryPoint_Program0.resolve(this)
    1 -> PropertyEntryPoint_Program1.resolve(this)
    2 -> PropertyEntryPoint_Program2.resolve(this)
    else -> error("Invalid identifier $id")
  } as T
}
