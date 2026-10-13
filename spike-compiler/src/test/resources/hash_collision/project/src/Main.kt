// "Aa" and "BB" share String.hashCode(), so every generated identity derived from these names collides.
fun main() {
    val ep = PropertyEntryPoint()
    require(ep.aa.name == "Aa") { "aa resolved to ${ep.aa.name}" }
    require(ep.bb.name == "BB") { "bb resolved to ${ep.bb.name}" }
    require(ep.holder.aa.name == "Aa") { "holder.aa resolved to ${ep.holder.aa.name}" }
    require(ep.holder.bb.name == "BB") { "holder.bb resolved to ${ep.holder.bb.name}" }
}

@spike.EntryPoint
interface PropertyEntryPoint {
    val aa: Aa
    val bb: BB
    val holder: Holder

    companion object
}

@spike.Include
class Aa {
    val name = "Aa"
}

@spike.Include
class BB {
    val name = "BB"
}

@spike.Include
class Holder(val aa: Aa, val bb: BB)
