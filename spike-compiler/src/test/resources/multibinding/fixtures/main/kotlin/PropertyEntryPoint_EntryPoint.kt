@file:Suppress("ClassName", "RedundantVisibilityModifier")

import kotlin.Suppress

private object PropertyEntryPoint_EntryPoint {
  public object Factory {
    public fun create(): PropertyEntryPoint = PropertyEntryPoint_Factory()
  }
}

public operator fun PropertyEntryPoint.Companion.invoke(): PropertyEntryPoint = PropertyEntryPoint_EntryPoint.Factory.create()
