fun main() {
    check(PropertyEntryPoint().cat is Sphynx)
}

@spike.EntryPoint
interface PropertyEntryPoint {
    val cat: Cat

    companion object
}

interface Cat

@spike.Include(bindAs = Cat::class)
class Sphynx : Cat