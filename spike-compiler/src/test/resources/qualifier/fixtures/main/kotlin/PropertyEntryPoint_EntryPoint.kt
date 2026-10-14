@file:Suppress("ClassName", "RedundantVisibilityModifier")

import kotlin.Suppress

private object PropertyEntryPoint_EntryPoint {
  public object Factory : PropertyEntryPoint.Factory {
    override fun create(food: Food): PropertyEntryPoint = PropertyEntryPoint_Factory(food)
  }
}

public operator fun PropertyEntryPoint.Companion.invoke(food: Food): PropertyEntryPoint = PropertyEntryPoint_EntryPoint.Factory.create(food)

public fun PropertyEntryPoint.Companion.factory(): PropertyEntryPoint.Factory = PropertyEntryPoint_EntryPoint.Factory
