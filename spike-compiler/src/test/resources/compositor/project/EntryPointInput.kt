@spike.EntryPoint
interface PropertyEntryPoint {
    val obj: ComplexObject

    companion object
}

@spike.Include
class Cat

@spike.Include
class ComplexObject(
    val cat: Cat,
    val box: Box,
    val teleportedCat: TeleportedCat
) {
    init {
        require(cat === box.cat)
        require(box === teleportedCat.box)
        require(teleportedCat.cat === cat)
        require(teleportedCat.box.cat === cat)
    }
}

@spike.Include
class Box(
    val cat: Cat
)

@spike.Include
class TeleportedCat(
    val box: Box,
    val cat: Cat
)