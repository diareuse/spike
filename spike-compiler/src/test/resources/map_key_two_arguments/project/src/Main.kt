@spike.EntryPoint
interface Shelter {
    val cats: Map<String, Cat>

    companion object
}

@spike.Key
annotation class Named(val first: String, val last: String)

class Cat

@spike.Include(bindTo = spike.BindTarget.Map)
@Named("tom", "cat")
fun cat(): Cat = Cat()
