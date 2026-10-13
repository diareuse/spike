@spike.EntryPoint
interface PropertyEntryPoint {
    val cats: Map<String, Cat>

    companion object
}

@spike.Key
annotation class StringKey(val name: String)

@StringKey("tom")
@spike.Include(bindTo = spike.BindTarget.Map)
class Cat {
    var name = "Tom"
}

@StringKey("tom")
@spike.Include(bindTo = spike.BindTarget.Map)
fun provideFelix(): Cat = Cat().apply { name = "Felix" }
