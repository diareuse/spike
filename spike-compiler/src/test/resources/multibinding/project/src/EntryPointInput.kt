@spike.EntryPoint
interface PropertyEntryPoint {
    val catsWithNames: Map<String, Cat>
    val cats: List<Cat>
    val uniqueCats: Set<Cat>

    companion object
}

@spike.Key
annotation class StringKey(val name: String)

data class Cat(val name: String)

@spike.Include(bindTo = spike.BindTarget.Map)
@StringKey("sphynx")
fun sphynx() = Cat("sphynx")

@spike.Include(bindTo = spike.BindTarget.Map)
@StringKey("ragamuffin")
fun ragamuffin() = Cat("ragamuffin")


@spike.Include(bindTo = spike.BindTarget.List)
fun toyger() = Cat("toyger")

@spike.Include(bindTo = spike.BindTarget.List)
fun bombay() = Cat("bombay")


@spike.Include(bindTo = spike.BindTarget.Set)
fun ragdoll() = Cat("ragdoll")

@spike.Include(bindTo = spike.BindTarget.Set)
fun abyssinian() = Cat("abyssinian")
