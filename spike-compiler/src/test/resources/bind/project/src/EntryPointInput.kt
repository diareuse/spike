@spike.EntryPoint
interface PropertyEntryPoint {
    val cat: Cat

    companion object
}

interface Cat

@spike.Include(bindAs = Cat::class)
class Sphynx : Cat