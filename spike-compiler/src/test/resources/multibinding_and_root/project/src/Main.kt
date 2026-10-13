fun main() {
    val ep = PropertyEntryPoint()
    require(ep.cat.name == "root") { "cat resolved to ${ep.cat.name}" }
    require(ep.cats.map { it.name }.toSet() == setOf("abyssinian", "bengal")) { "cats resolved to ${ep.cats.map { it.name }}" }
    require(ep.catList.map { it.name }.toSet() == setOf("siamese")) { "catList resolved to ${ep.catList.map { it.name }}" }
}

@spike.EntryPoint
interface PropertyEntryPoint {
    val cats: Set<Cat>
    val catList: List<Cat>
    val cat: Cat

    companion object
}

@spike.Include
class Cat {
    var name = "root"
}

@spike.Include(bindTo = spike.BindTarget.Set)
fun abyssinian(): Cat = Cat().apply { name = "abyssinian" }

@spike.Include(bindTo = spike.BindTarget.Set)
fun bengal(): Cat = Cat().apply { name = "bengal" }

@spike.Include(bindTo = spike.BindTarget.List)
fun siamese(): Cat = Cat().apply { name = "siamese" }
