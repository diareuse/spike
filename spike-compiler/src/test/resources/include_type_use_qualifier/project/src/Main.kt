fun main() {
    val home = Home()
    val cats = home.cats
    require(cats.wild.name == "wild" && cats.stray.name == "stray" && cats.tame.name == "tame") {
        "got ${cats.wild.name}, ${cats.stray.name}, ${cats.tame.name}"
    }
    require(home.stray.name == "stray") { "got ${home.stray.name}" }
}

@spike.Qualifier
@Target(AnnotationTarget.TYPE)
annotation class Wild

@spike.Qualifier
@Target(AnnotationTarget.TYPE)
annotation class Stray

@spike.EntryPoint
interface Home {
    val cats: Cats
    val stray: @Stray Cat

    companion object
}

class Cat(val name: String)

@spike.Include
fun wildCat(): @Wild Cat = Cat("wild")

@spike.Include
val strayCat: @Stray Cat get() = Cat("stray")

@spike.Include
fun tameCat(): Cat = Cat("tame")

@spike.Include
class Cats(val wild: @Wild Cat, val stray: @Stray Cat, val tame: Cat)
