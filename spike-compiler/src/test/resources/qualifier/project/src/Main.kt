fun main() {
    PropertyEntryPoint(Food()).apply {
        check(cat.name == "Sphynx")
        check(normalCat.name == "black")
    }
}

@spike.EntryPoint
interface PropertyEntryPoint {
    @Kind("sphynx")
    val cat: Cat
    val normalCat: Cat
    val catt: Map<CatType, Cat>

    @spike.EntryPoint.Factory
    interface Factory {
        fun create(@All food: Food): PropertyEntryPoint
    }
    companion object
}

@spike.Qualifier
annotation class All

@spike.Qualifier
annotation class Kind(val name: String)

@spike.Key
annotation class EnumKind(val type: CatType)

enum class CatType {
    Red, Green
}

class Food

interface Cat {
    val name: String
}

@Kind("sphynx")
@spike.Include(bindAs = Cat::class)
class Sphynx(@param:All food: Food) : Cat {
    override val name = "Sphynx"
}

class SomeCat(override val name: String) : Cat

@Kind("nyan")
@spike.Include(bindAs = Cat::class)
fun nyanCat(@All food: Food) = SomeCat("nyan")

@spike.Include(bindAs = Cat::class)
fun defaultCat() = SomeCat("black")

@EnumKind(CatType.Red)
@spike.Include(bindTo = spike.BindTarget.Map, bindAs = Cat::class)
fun redCat() = SomeCat("red")
