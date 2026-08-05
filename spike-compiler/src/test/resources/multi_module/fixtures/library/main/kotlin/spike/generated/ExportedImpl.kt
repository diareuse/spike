@file:Suppress("ClassName", "RedundantVisibilityModifier")

package spike.generated

import Exported
import Exported_Factory
import Lib
import kotlin.String
import kotlin.Suppress
import spike.Provider

public class ExportedImpl(
  string: Provider<String>,
) : Exported {
  private val factory: Exported_Factory = Exported_Factory(_string = string)

  override val lib: Lib
    get() = factory.get(spike.factory.DependencyId(0))
}
