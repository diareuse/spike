@spike.EntryPoint
interface PropertyEntryPoint {
    val cats: Map<kotlin.reflect.KClass<out Cat>, Cat>

    companion object
}

interface Cat