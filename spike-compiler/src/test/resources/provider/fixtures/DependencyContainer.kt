internal class DependencyContainer() {
  public inline val cat: Cat
    get() = Cat(food = ::food)

  private inline val food: Food
    get() = Food()
}
