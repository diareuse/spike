import kotlin.reflect.KClass

@spike.EntryPoint
interface Shelter {
    val cats: Map<KClass<*>, Cat>

    companion object
}

@spike.Key
annotation class ClassKey(val value: KClass<*>)

class Cat

@spike.Include(bindTo = spike.BindTarget.Map)
@ClassKey(Breed::class)
fun cat(): Cat = Cat()
