lateinit var dog: Dog
fun main() {
    dog = Dog()
    PropertyEntryPoint().cat
}

@spike.Include
fun inputParameterDog(): Dog = dog

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

class Dog