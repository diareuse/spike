internal class SpikeDependencyContainer() {
  public val cat: Cat
    get() = sphynx as Cat

  private inline val sphynx: Sphynx
    get() = Sphynx()
}
