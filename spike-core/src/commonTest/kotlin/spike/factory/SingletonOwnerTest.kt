package spike.factory

import kotlin.test.Test
import kotlin.test.assertSame

class SingletonOwnerTest {

    private class Owner(shared: SingletonHolder? = null) : SingletonOwner(shared)

    @Test
    fun `an owner without a shared holder keeps one holder for its whole life`() {
        val owner = Owner()

        assertSame(owner._singletons, owner._singletons)
    }

    @Test
    fun `an owner given a shared holder uses exactly that holder`() {
        val shared = SingletonHolder()

        assertSame(shared, Owner(shared)._singletons)
    }

    @Test
    fun `owners given the same holder share their singletons`() {
        val shared = SingletonHolder()
        val first = Owner(shared)._singletons.getOrPut(0) { Any() }

        val second = Owner(shared)._singletons.getOrPut<Any>(0) { error("must reuse the shared instance") }

        assertSame(first, second)
    }

}
