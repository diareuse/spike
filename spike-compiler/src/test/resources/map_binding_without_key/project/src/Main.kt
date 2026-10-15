@spike.EntryPoint
interface Shelter {
    val cats: Map<String, Cat>

    companion object
}

class Cat

@spike.Include(bindTo = spike.BindTarget.Map)
fun cat(): Cat = Cat()
