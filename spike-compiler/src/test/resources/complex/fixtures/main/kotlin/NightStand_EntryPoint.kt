@file:Suppress("ClassName", "RedundantVisibilityModifier")

import kotlin.Suppress

private object NightStand_EntryPoint {
  public object Factory {
    public fun create(): NightStand = NightStand_Factory()
  }
}

public operator fun NightStand.Companion.invoke(): NightStand = NightStand_EntryPoint.Factory.create()
