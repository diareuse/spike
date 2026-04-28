@file:Suppress("ClassName", "RedundantVisibilityModifier")

import kotlin.Lazy
import kotlin.Suppress
import spike.Provider

private class PropertyEntryPoint_EntryPoint(
  private val factory: PropertyEntryPoint_Factory,
) : PropertyEntryPoint {
  override val cat: Cat
    get() = factory.get(spike.factory.DependencyId(0))

  override val cat2: Provider<Cat>
    get() = factory.get(spike.factory.DependencyId(3))

  override val cat3: Lazy<Cat>
    get() = factory.get(spike.factory.DependencyId(4))

  public object Factory {
    public fun create(): PropertyEntryPoint_EntryPoint = PropertyEntryPoint_EntryPoint(PropertyEntryPoint_Factory())
  }
}

public operator fun PropertyEntryPoint.Companion.invoke(): PropertyEntryPoint = PropertyEntryPoint_EntryPoint.Factory.create()
