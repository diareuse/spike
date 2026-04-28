@file:Suppress("ClassName", "RedundantVisibilityModifier")

import kotlin.Suppress

private class CatEP_EntryPoint(
  private val factory: CatEP_Factory,
) : CatEP {
  override val cat: Cat
    get() = factory.get(spike.factory.DependencyId(0))

  public object Factory {
    public fun create(): CatEP_EntryPoint = CatEP_EntryPoint(CatEP_Factory())
  }
}

public operator fun CatEP.Companion.invoke(): CatEP = CatEP_EntryPoint.Factory.create()
