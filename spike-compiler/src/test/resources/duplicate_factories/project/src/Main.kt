@spike.EntryPoint
interface PropertyEntryPoint {
    val cat: Cat

    companion object
}

class Cat(val name: String)

@spike.Include
fun provideTom(): Cat = Cat("Tom")

@spike.Include
fun provideFelix(): Cat = Cat("Felix")
