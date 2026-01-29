internal class SpikeDependencyContainer(
  private val dog: Dog,
) {
  public inline val cat: Cat
    get() {
      val dog: Dog = dog
      return Cat(
        dog = dog
      )
    }
}
