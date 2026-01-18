internal class SpikeDependencyContainer() {
  public val kindNameSphynxCat: Cat
    get() = kindNameSphynxSphynx as Cat

  public val cat: Cat
    get() = someCat as Cat

  private inline val kindNameSphynxSphynx: Sphynx
    get() = Sphynx()

  private inline val someCat: SomeCat
    get() = defaultCat()
}
