fun main() {
    val keeper = Shelter().keeper
    require(keeper.night.name == "night" && keeper.day.name == "day") { "got ${keeper.night.name}, ${keeper.day.name}" }
}

@spike.Qualifier
annotation class Night

@spike.EntryPoint
interface Shelter {
    val keeper: Keeper

    companion object
}

class Clock(val name: String)

@spike.Include
@Night
val nightClock: Clock get() = Clock("night")

@spike.Include
val dayClock: Clock get() = Clock("day")

@spike.Include
class Keeper(@Night val night: Clock, val day: Clock)
