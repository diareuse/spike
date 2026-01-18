internal class SpikeDependencyContainer(
  private val dog: Dog,
) {
  public val cat: Cat
    get() = Cat(dog = dog)
}
