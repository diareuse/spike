@spike.EntryPoint
interface Shelter {
    fun cat(name: String): Cat

    companion object
}

@spike.Include
class Cat(val name: String)
