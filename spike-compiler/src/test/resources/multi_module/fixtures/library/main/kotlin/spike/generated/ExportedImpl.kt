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
  kotlinString: Provider<String>,
  singletons: SingletonHolder = SingletonHolder(),
) : Exported {
  private val factory: Exported_Factory =
      Exported_Factory(_kotlinString = kotlinString, singletons = singletons)

  override val lib: Lib
    get() = factory.get(spike.factory.DependencyId(0))
}
