fun main() {
    var ep = PropertyEntryPoint()
    require(ep.cat === ep.cat)
    require(ep.cat === ep.cat2.get())
    require(ep.cat2.get() === ep.cat2.get())
    require(ep.cat3.value === ep.cat)
    val cat = ep.cat
    ep = PropertyEntryPoint()
    require(cat === ep.cat)
}

@spike.EntryPoint
interface PropertyEntryPoint {
    val cat: Cat
    val cat2: spike.Provider<Cat>
    val cat3: Lazy<Cat>

    companion object
}

@spike.Singleton
@spike.Include
class Cat(
    val food: spike.Provider<Food>
)

@spike.Include
class Food