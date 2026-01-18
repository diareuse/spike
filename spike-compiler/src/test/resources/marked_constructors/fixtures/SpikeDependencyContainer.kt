internal class SpikeDependencyContainer() {
  public inline val cat: Cat
    get() = Cat(one = life, two = life, three = life)

  private inline val life: Life
    get() = Life()
}
