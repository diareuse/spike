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

interface Cat {
    companion object {
        @spike.Include
        val HouseCat.asCat: Cat get() = this
    }
}

@spike.Singleton
@spike.Include
class HouseCat : Cat
