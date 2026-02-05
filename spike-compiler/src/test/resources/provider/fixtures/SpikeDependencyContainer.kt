import spike.Provider

internal class SpikeDependencyContainer() {
  public inline val cat: Cat
    get() {
      val providerOfFood = Provider<Food> {
          Food()
      }
      return Cat(
        food = providerOfFood
      )
    }
}
