@spike.EntryPoint
interface PropertyEntryPoint {
    val cat: Cat

    @spike.EntryPoint.Factory
    interface Factory {
        fun create(dog: Dog): PropertyEntryPoint
    }
    companion object
}

@spike.Include
class Cat(
    val dog: Dog
)

@spike.Include
class Dog