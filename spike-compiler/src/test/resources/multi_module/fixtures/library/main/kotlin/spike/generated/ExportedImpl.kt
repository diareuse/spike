@file:Suppress("ClassName", "RedundantVisibilityModifier")

package spike.generated

import Exported
import Exported_Factory
import Lib
import kotlin.Suppress

public object ExportedImpl : Exported {
  private val factory: Exported_Factory = Exported_Factory()

  override val lib: Lib
    get() = factory.get(spike.factory.DependencyId(0))
}
