@spike.EntryPoint
interface Shelter {
    val cat: Cat

    companion object
}

class Cat

class Bindings {
    @spike.Include
    fun cat(): Cat = Cat()
}
