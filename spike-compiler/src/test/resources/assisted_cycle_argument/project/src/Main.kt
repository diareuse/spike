@spike.EntryPoint
interface Application {
    val screen: Screen

    companion object
}

@spike.Include
class Detail(val id: String, val child: Child)

@spike.Include
class Child(val parent: spike.Provider<Detail>)

@spike.Include
class Screen(val detail: (String) -> Detail)
