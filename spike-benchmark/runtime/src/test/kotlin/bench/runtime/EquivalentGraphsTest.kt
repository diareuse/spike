package bench.runtime

import java.util.IdentityHashMap
import kotlin.test.Test
import kotlin.test.assertEquals

class EquivalentGraphsTest {

    private val graphs = mapOf(
        "manual" to { bench.manual.resolve() },
        "spike" to { bench.spike.resolve() },
        "dagger" to { bench.dagger.resolve() },
        "metro" to { bench.metro.resolve() },
        "kotlin-inject" to { bench.kotlininject.resolve() },
        "koin" to { bench.koin.resolve() }
    )

    @Test
    fun `every framework builds the same tree as the manual baseline`() {
        val expected = shapeOf(bench.manual.resolve())
        for ((name, resolve) in graphs) {
            assertEquals(expected, shapeOf(resolve()), "$name built a different graph")
        }
    }

    @Test
    fun `every resolution creates a new instance of every node`() {
        for ((name, resolve) in graphs) {
            val first = instancesOf(resolve())
            val second = instancesOf(resolve())
            assertEquals(2000, first.size, "$name graph size")
            assertEquals(0, first.keys.count { it in second }, "$name reused instances across resolutions")
        }
    }

    private fun shapeOf(node: Any): String =
        node.javaClass.simpleName + node.dependencies().joinToString(",", "(", ")") { shapeOf(it) }

    private fun instancesOf(root: Any): IdentityHashMap<Any, Unit> {
        val seen = IdentityHashMap<Any, Unit>()
        val pending = ArrayDeque(listOf(root))
        while (pending.isNotEmpty()) {
            val node = pending.removeFirst()
            if (seen.put(node, Unit) == null) pending.addAll(node.dependencies())
        }
        return seen
    }

    private fun Any.dependencies(): List<Any> = javaClass.declaredFields
        .filter { it.type.name.startsWith("bench.") }
        .sortedBy { it.name }
        .map { it.isAccessible = true; it.get(this) }

}
