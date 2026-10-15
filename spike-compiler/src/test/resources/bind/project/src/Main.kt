fun main() {
    check(PropertyEntryPoint().cat is Sphynx)
}

@spike.EntryPoint
interface PropertyEntryPoint {
    val cat: Cat

    companion object
}

interface Cat

@spike.Include
class Sphynx : Cat

@spike.Include
val Sphynx.asCat: Cat get() = this