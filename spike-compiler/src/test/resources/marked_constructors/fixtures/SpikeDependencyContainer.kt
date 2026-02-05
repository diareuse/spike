internal class SpikeDependencyContainer() {
  public inline val cat: Cat
    get() {
      val life: Life = Life()
      return Cat(
        one = life,
        two = life,
        three = life
      )
    }
}
