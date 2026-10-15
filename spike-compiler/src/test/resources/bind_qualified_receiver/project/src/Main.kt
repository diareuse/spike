fun main() {
    val pet = Home().pet
    require(pet.name == "wild") { "got ${pet.name}" }
}

@spike.Qualifier
@Target(AnnotationTarget.TYPE, AnnotationTarget.FUNCTION)
annotation class Wild

@spike.EntryPoint
interface Home {
    val pet: Pet

    companion object
}

interface Pet {
    val name: String
}

class Cat(override val name: String) : Pet

@spike.Include
@Wild
fun wildCat(): Cat = Cat("wild")

@spike.Include
fun tameCat(): Cat = Cat("tame")

@spike.Include
val (@Wild Cat).asPet: Pet get() = this
