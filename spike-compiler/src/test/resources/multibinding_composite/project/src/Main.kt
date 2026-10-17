import spike.BindTarget
import spike.EntryPoint
import spike.Include

interface Step {
    fun run(): String
}

@Include
class First : Step {
    override fun run() = "first"
}

@Include(bindTo = BindTarget.Set)
val First.asStep: Step get() = this

@Include
class AllSteps(private val steps: Set<Step>) : Step {
    override fun run() = steps.joinToString { it.run() }
}

@Include
val AllSteps.asStep: Step get() = this

@EntryPoint
interface App {
    val step: Step

    companion object
}

fun main() {
    check(App().step.run() == "first") { App().step.run() }
}
