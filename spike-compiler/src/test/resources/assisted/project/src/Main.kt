fun main() {
    val screen = Application().screen
    val first = screen.detail("42")
    val second = screen.detail("7")
    check(first.id == "42" && second.id == "7") { "ids ${first.id} ${second.id}" }
    check(first !== second)
    check(first.repo === second.repo) { "the singleton repository was built twice" }
    val badge = screen.badge(3, "new")
    check(badge.count == 3 && badge.label == "new") { "badge ${badge.count} ${badge.label}" }
}

@spike.EntryPoint
interface Application {
    val screen: Screen

    companion object
}

@spike.Singleton
@spike.Include
class Repo

@spike.Include
class Detail(val id: String, val repo: Repo)

@spike.Include
class Badge(val count: Int, val label: String)

@spike.Include
class Screen(val detail: (String) -> Detail, val badge: (Int, String) -> Badge)
