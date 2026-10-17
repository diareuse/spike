import spike.BindTarget
import spike.EntryPoint
import spike.Include

interface Box<T> {
    fun name(): String
}

@Include
class TextBox : Box<String> {
    override fun name() = "text"
}

@Include
val TextBox.asBox: Box<*> get() = this

@Include(bindTo = BindTarget.Set)
val TextBox.asBoxEntry: Box<*> get() = this

@EntryPoint
interface App {
    val box: Box<*>
    val boxes: Set<Box<*>>

    companion object
}

fun main() {
    val app = App()
    check(app.box.name() == "text")
    check(app.boxes.single().name() == "text")
}
