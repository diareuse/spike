internal class SpikeDependencyContainer() {
  public inline val cat: Cat
    get() {
      val life: Life = life
      return Cat(
        one = life,
        two = life,
        three = life
      )
    }

  private inline val life: Life
    get() = Life()
}
