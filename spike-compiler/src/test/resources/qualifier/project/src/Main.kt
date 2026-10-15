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
@spike.Include
val Sphynx.asCat: Cat get() = this

@spike.Include
class Sphynx(@param:All food: Food) : Cat {
    override val name = "Sphynx"
}

class SomeCat(override val name: String) : Cat

@Kind("nyan")
@spike.Include
fun nyanCat(@All food: Food): Cat = SomeCat("nyan")

@spike.Include
fun defaultCat(): Cat = SomeCat("black")

@EnumKind(CatType.Red)
@spike.Include(bindTo = spike.BindTarget.Map)
fun redCat(): Cat = SomeCat("red")
