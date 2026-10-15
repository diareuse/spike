@spike.EntryPoint
interface Shelter {
    val cat: Cat

    companion object
}

interface Cat

@spike.Include
class HouseCat : Cat

class Bindings {
    @spike.Include
    val HouseCat.asCat: Cat get() = this
}
