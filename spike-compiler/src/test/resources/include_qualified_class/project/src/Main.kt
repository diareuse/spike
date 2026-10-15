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

@Night
@spike.Include
class Clock {
    var name = "night"
}

@spike.Include
fun dayClock(): Clock = Clock().apply { name = "day" }

@spike.Include
class Keeper(@Night val night: Clock, val day: Clock)
