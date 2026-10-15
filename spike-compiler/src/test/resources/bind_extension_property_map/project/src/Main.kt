fun main() {
    val animals = Shelter().animals
    require(animals.mapValues { it.value.name } == mapOf("fish" to "fish")) { "animals were $animals" }
}

@spike.Key
annotation class Named(val name: String)

@spike.EntryPoint
interface Shelter {
    val animals: Map<String, Animal>

    companion object
}

interface Animal {
    val name: String
}

@spike.Include
class Fish : Animal {
    override val name = "fish"
}

@spike.Include(bindTo = spike.BindTarget.Map)
@Named("fish")
val Fish.asAnimal: Animal get() = this
