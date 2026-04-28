@file:Suppress("ClassName", "RedundantVisibilityModifier")

import kotlin.Suppress

private class NightStand_EntryPoint(
  private val factory: NightStand_Factory,
) : NightStand {
  override val television: Television
    get() = factory.get(spike.factory.DependencyId(0))

  override val remote: Remote
    get() = factory.get(spike.factory.DependencyId(5))

  public object Factory {
    public fun create(): NightStand_EntryPoint = NightStand_EntryPoint(NightStand_Factory())
  }
}

public operator fun NightStand.Companion.invoke(): NightStand = NightStand_EntryPoint.Factory.create()
