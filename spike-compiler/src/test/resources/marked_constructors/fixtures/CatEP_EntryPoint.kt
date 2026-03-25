private object CatEP_EntryPoint : CatEP {
  override val cat: Cat
    get() = CatEP_Factory.get(spike.factory.DependencyId(0))
}

public operator fun CatEP.Companion.invoke(): CatEP = CatEP_EntryPoint
