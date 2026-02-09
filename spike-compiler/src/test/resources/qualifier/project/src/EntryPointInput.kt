@spike.EntryPoint
interface PropertyEntryPoint {
    @Kind("sphynx")
    val cat: Cat
    val normalCat: Cat

    companion object
}

@spike.Qualifier
annotation class Kind(val name: String)

interface Cat {
    val name: String
}

@Kind("sphynx")
@spike.Include(bindAs = Cat::class)
class Sphynx : Cat {
    override val name = "Sphynx"
}

class SomeCat(override val name: String) : Cat

@Kind("nyan")
@spike.Include(bindAs = Cat::class)
fun nyanCat() = SomeCat("nyan")

@spike.Include(bindAs = Cat::class)
fun defaultCat() = SomeCat("black")
