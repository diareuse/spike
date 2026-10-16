fun main() {
    val detail = Application().screen.detail("42")
    check(detail.labels.map { it.text } == listOf("label 42")) { "labels ${detail.labels.map { it.text }}" }
}

@spike.EntryPoint
interface Application {
    val screen: Screen

    companion object
}

class Label(val text: String)

@spike.Include(bindTo = spike.BindTarget.List)
fun label(id: String) = Label("label $id")

@spike.Include
class Detail(val id: String, val labels: List<Label>)

@spike.Include
class Screen(val detail: (String) -> Detail)
