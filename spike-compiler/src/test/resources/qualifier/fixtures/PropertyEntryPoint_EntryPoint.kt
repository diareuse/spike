private object PropertyEntryPoint_EntryPoint : PropertyEntryPoint {
  override val cat: Cat
    get() = PropertyEntryPoint_Factory.get(spike.factory.DependencyId(0))

  override val normalCat: Cat
    get() = PropertyEntryPoint_Factory.get(spike.factory.DependencyId(2))
}

public operator fun PropertyEntryPoint.Companion.invoke(): PropertyEntryPoint = PropertyEntryPoint_EntryPoint
