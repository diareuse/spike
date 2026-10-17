package spike.compiler.graph

import spike.compiler.diagnostic.DiagnosticException
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertFailsWith

class SharedSingletonInputCheckTest {

    private val entryType = Type.Simple("test", "Entry", false)
    private val factoryType = Type.Simple("test", "EntryFactory", false)
    private val input = TypeFactory.Property(Type.Simple("test", "Handle", false), "handle")

    @Test
    fun `a singleton depending on a per-instance input fails naming both`() {
        val singleton = node("Cache", input, singleton = true)

        val error = assertFailsWith<DiagnosticException> { check(listOf(singleton)) }

        assertContains(error.message!!, "@Singleton test.Cache depends on test.Handle")
    }

    @Test
    fun `a singleton thousands of levels above a per-instance input still fails`() {
        val singleton = node("Cache", chain(20_000, input), singleton = true)

        val error = assertFailsWith<DiagnosticException> { onSmallStack { check(listOf(singleton)) } }

        assertContains(error.message!!, "@Singleton test.Cache depends on test.Handle")
    }

    @Test
    fun `a deep chain of non-singletons over a per-instance input passes on a small thread stack`() {
        val root = chain(20_000, input)

        onSmallStack { check(listOf(root)) }
    }

    @Test
    fun `a singleton reaching an input only through a Provider fails`() {
        val target = node("Session", input)
        val providerType = Type.Parametrized(BuiltInTypes.Provider, listOf(target.type), false)
        val provider = TypeFactory.Provides(providerType, target)
        val singleton = node("Cache", provider, singleton = true)

        val error = assertFailsWith<DiagnosticException> { check(listOf(singleton)) }

        assertContains(error.message!!, "@Singleton test.Cache depends on test.Handle")
    }

    @Test
    fun `a singleton without a path to an input passes`() {
        val singleton = node("Cache", node("Clock"), singleton = true)

        check(listOf(singleton, node("Session", input)))
    }

    @Test
    fun `entry points that do not share singletons are not checked`() {
        val singleton = node("Cache", input, singleton = true)

        check(listOf(singleton), ownership = SingletonOwnership.PerEntryPoint)
    }

    @Test
    fun `the first singleton in dependency order is the one reported`() {
        val first = node("FirstCache", input, singleton = true)
        val second = node("SecondCache", input, singleton = true)

        val error = assertFailsWith<DiagnosticException> { check(listOf(node("Root", first, second))) }

        assertContains(error.message!!, "@Singleton test.FirstCache depends")
    }

    @Test
    fun `a singleton reached from a later root over an input seen by an earlier root fails`() {
        val session = node("Session", input)
        val singleton = node("Cache", session, singleton = true)

        val error = assertFailsWith<DiagnosticException> { check(listOf(node("Screen", session), singleton)) }

        assertContains(error.message!!, "@Singleton test.Cache depends on test.Handle")
    }

    @Test
    fun `a singleton reaching an input only through a Lazy fails`() {
        val target = node("Session", input)
        val lazy = TypeFactory.Memorizes(Type.Parametrized(BuiltInTypes.Lazy, listOf(target.type), false), target)
        val singleton = node("Cache", lazy, singleton = true)

        val error = assertFailsWith<DiagnosticException> { check(listOf(singleton)) }

        assertContains(error.message!!, "@Singleton test.Cache depends on test.Handle")
    }

    private fun check(roots: List<TypeFactory>, ownership: SingletonOwnership = SingletonOwnership.SharedByFactory) {
        val factory = GraphEntryPoint.Factory(
            type = factoryType,
            method = Member.Method("test", "create", entryType, factoryType),
            ownership = ownership,
        )
        val entry = GraphEntryPoint(entryType, factory, emptyList(), emptyList(), GraphRole.App)
        ownership.inputCheck(GraphStore()).check(entry, roots)
    }

    private fun chain(depth: Int, bottom: TypeFactory): TypeFactory {
        var link = bottom
        for (index in depth - 1 downTo 0) link = node("Link$index", link)
        return link
    }

    private fun node(name: String, vararg dependencies: TypeFactory, singleton: Boolean = false) = TypeFactory.Class(
        type = Type.Simple("test", name, false),
        invocation = Invocation(emptyList(), false),
        singleton = singleton,
        dependencies = dependencies.toList(),
    )
}
