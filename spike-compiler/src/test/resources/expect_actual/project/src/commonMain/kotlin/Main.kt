@spike.EntryPoint
interface PropertyEntryPoint {
    val cat: Cat

    companion object
}

@spike.Include
expect class Cat {
    fun meow(): String
}
