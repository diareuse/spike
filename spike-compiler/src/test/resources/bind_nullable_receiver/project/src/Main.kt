fun main() {
    val pet = Home().pet
    require(pet.name == "cat") { "got ${pet.name}" }
}

@spike.EntryPoint
interface Home {
    val pet: Pet

    companion object
}

interface Pet {
    val name: String
}

class Cat : Pet {
    override val name = "cat"
}

@spike.Include
fun cat(): Cat? = Cat()

@spike.Include
val Cat?.asPet: Pet get() = checkNotNull(this)
