package spike.compiler.graph

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class ArgumentCaptorTest {

    private val argument = TypeFactory.Argument(Type.Simple("test", "Table", false), 0)
    private val order = Type.Simple("test", "Order", false)

    @Test
    fun `a singleton that reaches an argument captures it`() {
        val capture = ArgumentCaptorSingleton().captured(factory(singleton = true), argument)

        assertEquals("@Singleton", capture?.capturing)
    }

    @Test
    fun `a factory that is not a singleton captures nothing`() =
        assertNull(ArgumentCaptorSingleton().captured(factory(singleton = false), argument))

    @Test
    fun `a Provider that reaches an argument captures it and points at what it provides`() {
        val provider = TypeFactory.Provides(
            Type.Parametrized(BuiltInTypes.Provider, listOf(order), false),
            factory(singleton = false),
        )

        val capture = ArgumentCaptorDeferred().captured(provider, argument)

        assertEquals("Provider" to order, capture?.capturing to capture?.located)
    }

    @Test
    fun `a direct dependency is not deferred and captures nothing`() =
        assertNull(ArgumentCaptorDeferred().captured(factory(singleton = true), argument))

    private fun factory(singleton: Boolean) = TypeFactory.Class(
        type = order,
        invocation = Invocation(emptyList(), false),
        singleton = singleton,
        dependencies = listOf(argument),
    )
}
