@spike.EntryPoint
interface PropertyEntryPoint {
    val cat: Cat

    companion object
}

@spike.Include
class Cat {
    var name = "Tom"
}

@spike.Include
fun provideFelix(): Cat = Cat().apply { name = "Felix" }
