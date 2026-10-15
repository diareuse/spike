@spike.EntryPoint
interface Shelter {
    val cat: Cat
    val dog: Dog

    companion object
}

class Cat

class Dog

class CatBindings {
    @spike.Include
    fun cat(): Cat = Cat()
}

class DogBindings {
    @spike.Include
    fun dog(): Dog = Dog()
}
