fun main() {
    val shelter = Shelter()
    require(shelter.animals.map { it.name } == listOf("fish")) { "animals were ${shelter.animals}" }
    require(shelter.fish.name == "fish")
}

@spike.EntryPoint
interface Shelter {
    val animals: Set<Animal>
    val fish: Fish

    companion object
}

interface Animal {
    val name: String
}

@spike.Include
class Fish : Animal {
    override val name = "fish"
}

@spike.Include(bindTo = spike.BindTarget.Set)
val Fish.asAnimal: Animal get() = this
