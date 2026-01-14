internal class DependencyContainer() {
  public inline val kindNameSphynxCat: Cat
    get() = kindNameSphynxSphynx as Cat

  public inline val cat: Cat
    get() = someCat as Cat

  private inline val kindNameSphynxSphynx: Sphynx
    get() = Sphynx()

  private inline val someCat: SomeCat
    get() = defaultCat()
}
