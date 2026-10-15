fun main() {
    val first = Shelter()
    require(first.cat === first.cat) { "a @Singleton property was evaluated twice in one entry point" }
    require(first.cat !== Shelter().cat) { "two entry points shared a @Singleton property" }
}

@spike.EntryPoint
interface Shelter {
    val cat: Cat

    companion object
}

interface Cat

@spike.Include
class HouseCat : Cat

@spike.Singleton
@spike.Include
val HouseCat.asCat: Cat get() = this
