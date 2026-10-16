fun main() {
    val ep = PropertyEntryPoint()
    ep.cat
    ep.cat
    ep.bowl
    ep.bowl
    check(Food.created == 1) { "Food was created ${Food.created} times" }
}

@spike.EntryPoint
interface PropertyEntryPoint {
    val cat: Cat
    val bowl: Bowl

    companion object
}

@spike.Singleton
@spike.Include
class Cat(val food: Food)

@spike.Include
class Bowl(val cat: Cat)

@spike.Include
class Food {
    init {
        created++
    }

    companion object {
        var created = 0
    }
}
