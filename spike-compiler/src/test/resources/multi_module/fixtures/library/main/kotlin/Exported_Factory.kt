@file:Suppress("ClassName", "RedundantVisibilityModifier")

import kotlin.String
import kotlin.Suppress
import spike.Provider
import spike.factory.DependencyId
import spike.factory.SingletonHolder

public class Exported_Factory(
  private val _kotlinString: Provider<String>,
  public val singletons: SingletonHolder = SingletonHolder(),
) {
  public val kotlinString: String
    get() = _kotlinString.get()

  @Suppress("UNCHECKED_CAST")
  public fun <T> `get`(id: DependencyId): T = when (id.id) {
    0 -> Exported_Program0.resolve(this)
    else -> error("Invalid identifier $id")
  } as T
}
