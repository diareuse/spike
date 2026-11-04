@file:Suppress("ClassName", "RedundantVisibilityModifier")

import kotlin.Suppress

private object Odd_EntryPoint {
  public object Factory {
    public fun create(): Odd = Odd_Factory()
  }
}

public operator fun Odd.Companion.invoke(): Odd = Odd_EntryPoint.Factory.create()
