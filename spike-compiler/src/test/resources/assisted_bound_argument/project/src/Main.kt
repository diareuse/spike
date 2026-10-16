@spike.EntryPoint
interface Application {
    val screen: Screen

    companion object
}

@spike.Include
fun name(): String = "graph"

@spike.Include
class Detail(val id: String)

@spike.Include
class Screen(val detail: (String) -> Detail)
