import spike.Provider

internal class SpikeDependencyContainer() {
  public inline val cat: Cat
    get() {
      val providerOfFood: Provider<Food> = Provider(::food)
      return Cat(
        food = providerOfFood
      )
    }

  private inline val food: Food
    get() = Food()
}
