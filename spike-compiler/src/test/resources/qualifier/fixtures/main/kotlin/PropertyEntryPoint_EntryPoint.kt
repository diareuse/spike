@file:Suppress("ClassName", "RedundantVisibilityModifier")

import kotlin.Suppress

private class PropertyEntryPoint_EntryPoint(
  private val factory: PropertyEntryPoint_Factory,
) : PropertyEntryPoint {
  override val cat: Cat
    get() = factory.get(spike.factory.DependencyId(0))

  override val normalCat: Cat
    get() = factory.get(spike.factory.DependencyId(3))

  public object Factory : PropertyEntryPoint.Factory {
    override fun create(food: Food): PropertyEntryPoint = PropertyEntryPoint_EntryPoint(PropertyEntryPoint_Factory(food))
  }
}

public operator fun PropertyEntryPoint.Companion.invoke(food: Food): PropertyEntryPoint = PropertyEntryPoint_EntryPoint.Factory.create(food)

public fun PropertyEntryPoint.Companion.factory(): PropertyEntryPoint.Factory = PropertyEntryPoint_EntryPoint.Factory
