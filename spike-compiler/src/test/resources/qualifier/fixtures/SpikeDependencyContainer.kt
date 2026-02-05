internal class SpikeDependencyContainer() {
  public inline val kindNameSphynxCat: Cat
    get() = Sphynx()

  public inline val cat: Cat
    get() = defaultCat()
}
