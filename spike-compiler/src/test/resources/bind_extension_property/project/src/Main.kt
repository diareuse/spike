fun main() {
    val shelter = Shelter()
    require(shelter.cat === shelter.houseCat) { "Cat was not the HouseCat singleton" }
}

@spike.EntryPoint
interface Shelter {
    val cat: Cat
    val houseCat: HouseCat

    companion object
}

interface Cat

@spike.Singleton
@spike.Include
class HouseCat : Cat

@spike.Include
val HouseCat.asCat: Cat get() = this
