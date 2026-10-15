import kotlin.reflect.KClass

fun main() {
    checkNotNull(PropertyEntryPoint().cats[SomeCat::class])
}

@spike.EntryPoint
interface PropertyEntryPoint {
    val cats: Map<kotlin.reflect.KClass<out Cat>, Cat>

    companion object
}

interface Cat

@spike.Key
annotation class CatKey(val klass: KClass<out Cat>)

class SomeCat : Cat

@spike.Include(bindTo = spike.BindTarget.Map)
@CatKey(SomeCat::class)
fun someCat(): Cat = SomeCat()