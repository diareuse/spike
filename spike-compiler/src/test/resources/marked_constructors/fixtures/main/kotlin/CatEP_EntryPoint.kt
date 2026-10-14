@file:Suppress("ClassName", "RedundantVisibilityModifier")

import kotlin.Suppress

private object CatEP_EntryPoint {
  public object Factory {
    public fun create(): CatEP = CatEP_Factory()
  }
}

public operator fun CatEP.Companion.invoke(): CatEP = CatEP_EntryPoint.Factory.create()
