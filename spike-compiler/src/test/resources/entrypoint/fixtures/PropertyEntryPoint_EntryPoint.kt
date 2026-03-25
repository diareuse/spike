private object PropertyEntryPoint_EntryPoint : PropertyEntryPoint {
  override val injectable: Injectable
    get() = PropertyEntryPoint_Factory.get(spike.factory.DependencyId(0))
}

public operator fun PropertyEntryPoint.Companion.invoke(): PropertyEntryPoint = PropertyEntryPoint_EntryPoint
