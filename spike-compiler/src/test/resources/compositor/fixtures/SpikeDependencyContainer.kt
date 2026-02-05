internal class SpikeDependencyContainer() {
  public inline val complexObject: ComplexObject
    get() {
      val cat: Cat = Cat()
      val box: Box = Box(
        cat = cat
      )
      val teleportedCat: TeleportedCat = TeleportedCat(
        box = box,
        cat = cat
      )
      return ComplexObject(
        cat = cat,
        box = box,
        teleportedCat = teleportedCat
      )
    }
}
