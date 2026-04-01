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
    companion object
}

@spike.Include
class Cat(
    val dog: Dog
)

class Dog