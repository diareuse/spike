@spike.EntryPoint
interface Shelter {
    val cat: Cat

    companion object
}

@spike.Include
interface Cat
