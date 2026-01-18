internal class SpikeCatEP(
  private val container: SpikeDependencyContainer,
) : CatEP {
  override val cat: Cat
    get() = container.cat
}

public operator fun CatEP.Companion.invoke(): CatEP = SpikeCatEPFactory().create()
