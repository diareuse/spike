@spike.EntryPoint
interface Shelter {
    val cat: Cat

    companion object
}

interface Cat

@spike.Include(bindAs = Cat::class)
class HouseCat : Cat
