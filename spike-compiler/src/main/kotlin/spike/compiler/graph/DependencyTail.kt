package spike.compiler.graph

class DependencyTail(
    val type: Type,
    val parent: DependencyTail? = null
) {
    val isStart get() = parent == null
    fun then(type: Type) = DependencyTail(type, this)
    fun startsWith(type: Type): Boolean {
        var curr = this
        while (true) {
            curr = curr.parent ?: break
        }
        return curr.type == type
    }

    override fun toString(): String {
        var curr = parent
        var out = "$type"
        while (curr != null) {
            out += "\n"
            out += " -> " + (curr.type)
            curr = curr.parent
        }
        return out
    }
}