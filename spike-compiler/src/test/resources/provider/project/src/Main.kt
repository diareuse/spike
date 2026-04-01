fun main() {
    PropertyEntryPoint().cat.food
}

@spike.EntryPoint
interface PropertyEntryPoint {
    val cat: Cat

    companion object
}

@spike.Include
class Cat(
    val food: spike.Provider<Food>
)

@spike.Include
class Food