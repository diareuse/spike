fun main() {
    val graph = Graph()
    val parent = graph.parent
    val again = parent.child.parent.get()
    check(again !== parent) { "a Provider of a non-singleton must build a new instance" }
    check(again.child.parent.get() !== again)
    val singleton = graph.owner
    check(singleton.pet.owner.value === singleton) { "a Lazy of a singleton must return the same instance" }
}

@spike.EntryPoint
interface Graph {
    val parent: Parent
    val owner: Owner

    companion object
}

@spike.Include
class Parent(val child: Child)

@spike.Include
class Child(val parent: spike.Provider<Parent>)

@spike.Singleton
@spike.Include
class Owner(val pet: Pet)

@spike.Include
class Pet(val owner: Lazy<Owner>)
