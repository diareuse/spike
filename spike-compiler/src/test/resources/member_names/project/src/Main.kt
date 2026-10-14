fun main() {
    val cat = Cat()
    val graph = MemberNames(cat)
    check(graph.cat === cat)
    check(graph.singletons is Food)
    check(graph.get is Food)
    check(graph.get() is Food)
}

@spike.EntryPoint
interface MemberNames {
    val cat: Cat
    val singletons: Food
    val get: Food
    fun get(): Food

    @spike.EntryPoint.Factory
    interface Factory {
        fun create(cat: Cat): MemberNames
    }

    companion object
}

class Cat

@spike.Include
class Food
