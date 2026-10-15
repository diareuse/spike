@spike.EntryPoint
interface Shelter {
    val cat: Cat

    companion object
}

interface Cat

class Sphynx : Cat

@spike.Include(bindAs = Cat::class)
fun sphynx(): Sphynx = Sphynx()
