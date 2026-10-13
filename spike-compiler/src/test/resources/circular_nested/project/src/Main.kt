@spike.EntryPoint
interface PropertyEntryPoint {
    val root: Root

    companion object
}

@spike.Include
class Root(val cat: Cat)

@spike.Include
class Cat(val dog: Dog)

@spike.Include
class Dog(val cat: Cat)
