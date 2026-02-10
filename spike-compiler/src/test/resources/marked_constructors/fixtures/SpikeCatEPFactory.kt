internal object SpikeCatEPFactory {
  public fun create(): CatEP = SpikeCatEP(SpikeDependencyContainer())
}
