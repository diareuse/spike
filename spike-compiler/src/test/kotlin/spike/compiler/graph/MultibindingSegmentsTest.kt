package spike.compiler.graph

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertSame

class MultibindingSegmentsTest {

    private val segments = MultibindingSegments(size = 2)
    private val setType = Type.Parametrized(BuiltInTypes.Set, listOf(Type.Simple("test", "Plugin", false)), false)
    private val mapType = Type.Parametrized(
        BuiltInTypes.Map,
        listOf(Type(String::class, false), Type.Simple("test", "Plugin", false)),
        false,
    )

    @Test
    fun `a collection within the segment size stays whole`() {
        val collection = collection(entries = 2)

        assertSame(collection, segments.of(collection))
    }

    @Test
    fun `a larger collection is concatenated from segments of at most the segment size`() {
        val segmented = assertIs<TypeFactory.Concatenation>(segments.of(collection(entries = 5)))

        assertEquals(listOf(2, 2, 1), segmented.parts.map { (it as TypeFactory.MultibindsCollection).entries.size })
    }

    @Test
    fun `a larger map is concatenated from segments that keep every key`() {
        val map = TypeFactory.MultibindsMap(mapType, (1..3).associate { "k$it" to plugin(it) })

        val segmented = assertIs<TypeFactory.Concatenation>(segments.of(map))

        assertEquals(
            map.keyValues.keys,
            segmented.parts.flatMap { (it as TypeFactory.MultibindsMap).keyValues.keys }.toSet(),
        )
    }

    private fun collection(entries: Int) =
        TypeFactory.MultibindsCollection(setType, (1..entries).map(::plugin), BuiltInTypes.Set)

    private fun plugin(index: Int) = TypeFactory.Class(
        type = Type.Simple("test", "Plugin$index", false),
        invocation = Invocation(emptyList(), false),
        singleton = false,
        dependencies = emptyList(),
    )
}
