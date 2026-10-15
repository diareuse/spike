@spike.EntryPoint
interface Shelter {
    val cats: Map<String, Cat>

    companion object
}

@spike.Key
annotation class Named(val name: String)

@spike.Key
annotation class Nickname(val name: String)

class Cat

@spike.Include(bindTo = spike.BindTarget.Map)
@Named("cat")
@Nickname("kitty")
fun cat(): Cat = Cat()
