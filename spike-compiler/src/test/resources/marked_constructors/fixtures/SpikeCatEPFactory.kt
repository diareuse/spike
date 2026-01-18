internal class SpikeCatEPFactory {
  public fun create(): CatEP = SpikeCatEP(SpikeDependencyContainer())
}
