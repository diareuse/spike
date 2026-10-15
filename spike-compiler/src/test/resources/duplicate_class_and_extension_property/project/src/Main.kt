@spike.EntryPoint
interface Shelter {
    val cat: Cat

    companion object
}

interface Cat {
    companion object {
        @spike.Include
        val HouseCat.asCat: Cat get() = this
    }
}

@spike.Include
class HouseCat : Cat

@spike.Include
class StrayCat : Cat

@spike.Include
val StrayCat.asCat: Cat get() = this
