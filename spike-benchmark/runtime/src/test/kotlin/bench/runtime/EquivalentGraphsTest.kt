package bench.runtime

import java.util.IdentityHashMap
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class EquivalentGraphsTest {

    private val graphs = mapOf(
        "manual" to Framework({ bench.manual.createGraph() }, { bench.manual.resolve(it) }),
        "spike" to Framework({ bench.spike.createGraph() }, { bench.spike.resolve(it) }),
        "dagger" to Framework({ bench.dagger.createGraph() }, { bench.dagger.resolve(it) }),
        "metro" to Framework({ bench.metro.createGraph() }, { bench.metro.resolve(it) }),
        "kotlin-inject" to Framework({ bench.kotlininject.createGraph() }, { bench.kotlininject.resolve(it) }),
        "koin" to Framework({ bench.koin.createGraph() }, { bench.koin.resolve(it) })
    )

    private val screens = mapOf(
        "manual" to Screens(bench.manual.createApp()) { app, id -> bench.manual.openScreen(app, id).let { it.handle.id to it.state } },
        "spike" to Screens(bench.spike.createApp()) { app, id -> bench.spike.openScreen(app, id).let { it.handle.id to it.state } },
        "dagger" to Screens(bench.dagger.createApp()) { app, id -> bench.dagger.openScreen(app, id).let { it.handle.id to it.state } },
        "metro" to Screens(bench.metro.createApp()) { app, id -> bench.metro.openScreen(app, id).let { it.handle.id to it.state } },
        "kotlin-inject" to Screens(bench.kotlininject.createApp()) { app, id ->
            bench.kotlininject.openScreen(app, id).let { it.handle.id to it.state }
        },
        "koin" to Screens(bench.koin.createApp()) { app, id -> bench.koin.openScreen(app, id).let { it.handle.id to it.state } },
    )

    private val expectedSize = instancesOf(bench.manual.resolve()).size

    @Test
    fun `every framework opens screens with their own handle and the app's single state`() {
        for ((name, screens) in screens) {
            val (firstId, firstState) = screens.open(1)
            val (secondId, secondState) = screens.open(2)
            assertEquals(1 to 2, firstId to secondId, "$name passed the wrong handle")
            assertTrue(firstState === secondState, "$name built the app state twice")
        }
    }

    private class Screens<A>(private val app: A, private val open: (A, Int) -> Pair<Int, Any>) {
        fun open(id: Int) = open(app, id)
    }

    @Test
    fun `every framework builds the same tree as the manual baseline`() {
        val expected = shapeOf(bench.manual.resolve())
        for ((name, framework) in graphs) {
            assertEquals(expected, shapeOf(framework.resolveFresh()), "$name built a different graph")
        }
    }

    @Test
    fun `resolving twice from one live graph creates a new instance of every node`() {
        assertTrue(expectedSize > 1)
        for ((name, framework) in graphs) {
            val (first, second) = framework.resolveTwiceFromOneGraph()
            assertEquals(expectedSize, first.size, "$name graph size")
            assertEquals(0, first.keys.count { it in second }, "$name reused instances within one graph")
        }
    }

    private inner class Framework<G>(private val create: () -> G, private val resolve: (G) -> Any) {
        fun resolveFresh(): Any = resolve(create())
        fun resolveTwiceFromOneGraph(): Pair<IdentityHashMap<Any, Unit>, IdentityHashMap<Any, Unit>> {
            val graph = create()
            return instancesOf(resolve(graph)) to instancesOf(resolve(graph))
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
