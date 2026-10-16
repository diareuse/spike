@spike.EntryPoint
interface Application {
    val screen: Screen

    companion object
}

@spike.Include
class Range(val from: String, val to: String)

@spike.Include
class Screen(val range: (String, String) -> Range)
