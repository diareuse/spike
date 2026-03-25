@spike.EntryPoint
interface PropertyEntryPoint {
    val cat: Cat

    companion object
}

@spike.Include
class Cat(
    val dog: Dog
)

@spike.Include
class Dog(
    val cat: Cat
)