fun main() {
    val zoo = Zoo()
    check(zoo.cats.map { it.name }.toSet() == setOf("tom", "felix", "garfield")) { "cats ${zoo.cats}" }
    check(zoo.lineup.map { it.name } == listOf("first", "second", "third")) { "lineup ${zoo.lineup}" }
}

data class Cat(val name: String)

@spike.EntryPoint
interface Zoo {
    val cats: Set<Cat>
    val lineup: List<Cat>

    companion object
}

@spike.Include(bindTo = spike.BindTarget.Set)
fun tom() = Cat("tom")

@spike.Include(bindTo = spike.BindTarget.Set)
fun strays(): Set<Cat> = setOf(Cat("felix"), Cat("garfield"))

@spike.Include(bindTo = spike.BindTarget.List)
fun first() = Cat("first")

@spike.Include(bindTo = spike.BindTarget.List)
fun others(): List<Cat> = listOf(Cat("second"), Cat("third"))
