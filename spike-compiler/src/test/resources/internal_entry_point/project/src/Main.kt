import spike.EntryPoint
import spike.Include

internal interface Setup {
    fun name(): String
}

@Include
internal class DefaultSetup : Setup {
    override fun name() = "default"
}

@Include
internal val DefaultSetup.asSetup: Setup get() = this

@EntryPoint
internal interface Setups {
    val setup: Setup

    companion object
}

fun main() {
    check(Setups().setup.name() == "default")
}
