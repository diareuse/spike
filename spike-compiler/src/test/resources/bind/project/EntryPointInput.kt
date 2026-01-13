@spike.EntryPoint
interface PropertyEntryPoint {
    val cat: Cat

    companion object
}

interface Cat

@spike.Include
@spike.Bind(Cat::class)
class Sphynx : Cat