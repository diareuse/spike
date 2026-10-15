@file:Suppress("ClassName", "RedundantVisibilityModifier")

package spike.generated

import Exported
import Exported_Factory
import Lib
import kotlin.String
import kotlin.Suppress
import spike.Provider
import spike.factory.SingletonHolder

public class ExportedImpl(
  kotlin_String: Provider<String>,
  singletons: SingletonHolder = SingletonHolder(),
) : Exported {
  private val factory: Exported_Factory =
      Exported_Factory(_kotlin_String = kotlin_String, singletons = singletons)

  override val lib: Lib
    get() = factory.get(spike.factory.DependencyId(0))
}
