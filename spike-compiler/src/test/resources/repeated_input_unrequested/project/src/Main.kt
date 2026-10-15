fun main() {
    require(Configured.factory().create("a", "b").clock.name == "clock")
}

@spike.EntryPoint
interface Configured {
    val clock: Clock

    @spike.EntryPoint.Factory
    interface Factory {
        fun create(primary: String, secondary: String): Configured
    }

    companion object
}

@spike.Include
class Clock {
    val name = "clock"
}
