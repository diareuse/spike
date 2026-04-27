package benchmark.project.src

private class LargeTreeEntryPoint_EntryPoint(
  private val factory: LargeTreeEntryPoint_Factory,
) : LargeTreeEntryPoint {
  override val root: Root
    get() = factory.get(spike.factory.DependencyId(0))

  public object Factory {
    public fun create(): LargeTreeEntryPoint_EntryPoint = LargeTreeEntryPoint_EntryPoint(LargeTreeEntryPoint_Factory())
  }
}

public operator fun LargeTreeEntryPoint.Companion.invoke(): LargeTreeEntryPoint = LargeTreeEntryPoint_EntryPoint.Factory.create()
