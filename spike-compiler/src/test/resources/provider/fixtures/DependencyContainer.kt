import kotlin.TODO

internal class DependencyContainer() {
  public inline val cat: Cat
    get() = Cat(food = ::food)

  private val food: Food
    get() {
      TODO()
    }
}
