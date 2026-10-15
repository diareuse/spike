@file:Suppress("ClassName", "RedundantVisibilityModifier")

import kotlin.String
import kotlin.Suppress
import spike.Provider
import spike.factory.DependencyId
import spike.factory.SingletonHolder
import spike.factory.SingletonOwner

public class Exported_Factory(
  private val _kotlin_String: Provider<String>,
  singletons: SingletonHolder? = null,
) : SingletonOwner(singletons) {
  public val kotlin_String: String
    get() = _kotlin_String.get()

  @Suppress("UNCHECKED_CAST")
  public fun <T> `get`(id: DependencyId): T = when (id.id) {
    0 -> Exported_Program0.resolve(this)
    else -> error("Invalid identifier $id")
  } as T
}
