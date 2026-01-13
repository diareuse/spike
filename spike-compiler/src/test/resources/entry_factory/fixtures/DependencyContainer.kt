internal class DependencyContainer(
  private val dog: Dog,
) {
  public inline val cat: Cat
    get() = Cat(dog = dog)
}
