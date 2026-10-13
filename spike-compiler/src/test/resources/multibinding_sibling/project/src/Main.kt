fun main() {
    val ep = PropertyEntryPoint()
    val owner = ep.animals.filterIsInstance<Owner>().single()
    require(owner.cat.name == "root") { "owner.cat resolved to ${owner.cat.name}" }
    require(ep.animals.filterIsInstance<Cat>().single().name == "felix")
}

@spike.EntryPoint
interface PropertyEntryPoint {
    val animals: Set<Animal>

    companion object
}

interface Animal

@spike.Include
class Cat : Animal {
    var name = "root"
}

@spike.Include(bindTo = spike.BindTarget.Set, bindAs = Animal::class)
fun felix(): Cat = Cat().apply { name = "felix" }

@spike.Include(bindTo = spike.BindTarget.Set, bindAs = Animal::class)
class Owner(val cat: Cat) : Animal
