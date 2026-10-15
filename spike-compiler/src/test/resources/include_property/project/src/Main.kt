fun main() {
    require(Shelter().keeper.clock.name == "clock")
}

@spike.EntryPoint
interface Shelter {
    val keeper: Keeper

    companion object
}

class Clock {
    val name = "clock"
}

@spike.Include
val clock: Clock get() = Clock()

@spike.Include
class Keeper(val clock: Clock)
