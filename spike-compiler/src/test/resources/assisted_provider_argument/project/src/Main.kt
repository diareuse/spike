@spike.EntryPoint
interface Application {
    val screen: Screen

    companion object
}

@spike.Include
class Session(val id: String)

@spike.Include
class Detail(val session: spike.Provider<Session>)

@spike.Include
class Screen(val detail: (String) -> Detail)
