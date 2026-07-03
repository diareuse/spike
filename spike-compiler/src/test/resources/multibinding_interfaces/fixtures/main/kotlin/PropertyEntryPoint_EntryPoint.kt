@file:Suppress("ClassName", "RedundantVisibilityModifier")

import kotlin.String
import kotlin.Suppress
import kotlin.collections.Map

private class PropertyEntryPoint_EntryPoint(
  private val factory: PropertyEntryPoint_Factory,
) : PropertyEntryPoint {
  override val workers: Map<String, IntWorker>
    get() = factory.get(spike.factory.DependencyId(0))

  public object Factory {
    public fun create(): PropertyEntryPoint_EntryPoint = PropertyEntryPoint_EntryPoint(PropertyEntryPoint_Factory())
  }
}

public operator fun PropertyEntryPoint.Companion.invoke(): PropertyEntryPoint = PropertyEntryPoint_EntryPoint.Factory.create()
