package spike.compiler.graph

import spike.BindTarget
import spike.compiler.diagnostic.DiagnosticException
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertIs

class DependencyGraphTest {

    private val cat = type("Cat")
    private val food = type("Food")
    private val exported = type("ExportedImpl")

    @Test
    fun `a property resolves to its constructor with the constructor's dependencies`() {
        val graph = graph(cat) {
            constructor(cat, food)
            constructor(food)
        }

        val root = assertIs<TypeFactory.Class>(graph.properties.single())
        assertEquals(cat, root.type)
        assertEquals(listOf(food), root.dependencies.map { it.type })
    }

    @Test
    fun `a dependency without a binding fails as a missing binding`() {
        val error = assertFailsWith<DiagnosticException> { graph(cat) { constructor(cat, food) } }

        assertEquals(listOf("SPIKE-MISSING-BINDING"), error.diagnostics.map { it.code })
    }

    @Test
    fun `a dependency without a binding in a module becomes an import`() {
        val graph = graph(cat, module = true) { constructor(cat, food) }

        assertEquals(listOf(food), graph.importFactories.map { it.type })
    }

    @Test
    fun `two bindings of one type fail as a duplicate binding`() {
        val error = assertFailsWith<DiagnosticException> {
            graph(cat) {
                constructor(cat)
                addFactory(cat, Member.Method("test", "cat", cat), Invocation(emptyList(), false), false)
            }
        }

        assertEquals(listOf("SPIKE-DUPLICATE-BINDING"), error.diagnostics.map { it.code })
    }

    @Test
    fun `a type that depends on itself through another fails as a dependency cycle`() {
        val error = assertFailsWith<DiagnosticException> {
            graph(cat) {
                constructor(cat, food)
                constructor(food, cat)
            }
        }

        assertEquals(listOf("SPIKE-DEPENDENCY-CYCLE"), error.diagnostics.map { it.code })
    }

    @Test
    fun `a Provider request defers to the provided type`() {
        val graph = graph(parametrized(BuiltInTypes.Provider, cat)) { constructor(cat) }

        val root = assertIs<TypeFactory.Provides>(graph.properties.single())
        assertEquals(cat, root.factory.type)
    }

    @Test
    fun `a Lazy request memorizes the requested type`() {
        val graph = graph(parametrized(BuiltInTypes.Lazy, cat)) { constructor(cat) }

        val root = assertIs<TypeFactory.Memorizes>(graph.properties.single())
        assertEquals(cat, root.factory.type)
    }

    @Test
    fun `a factory parameter satisfies a dependency of the same type`() {
        val graph = graph(cat, parameters = listOf(Parameter("food", food, false))) { constructor(cat, food) }

        val input = graph.properties.single().dependencies.single()
        assertEquals(TypeFactory.Property(food, "food"), input)
    }

    @Test
    fun `a list without contributions resolves to an empty collection in an entry point`() {
        val graph = graph(parametrized(BuiltInTypes.List, cat))

        val root = assertIs<TypeFactory.MultibindsCollection>(graph.properties.single())
        assertEquals(emptyList(), root.entries)
    }

    @Test
    fun `a map without contributions resolves to an empty map in an entry point`() {
        val graph = graph(parametrized(BuiltInTypes.Map, Type.Simple("kotlin", "String", false), cat))

        val root = assertIs<TypeFactory.MultibindsMap>(graph.properties.single())
        assertEquals(emptyMap(), root.keyValues)
    }

    @Test
    fun `a member exported by a module satisfies a dependency of its type`() {
        val member = TypeFactory.External(food, "food", isMethod = false, origin = exported)
        val graph = graph(cat, imports = { putExternal(member, type("Exported")) }) { constructor(cat, food) }

        assertEquals(listOf(member), graph.properties.single().dependencies)
    }

    @Test
    fun `a list in a module becomes an import even when the module contributes to it`() {
        val list = parametrized(BuiltInTypes.List, cat)
        val graph = graph(list, module = true, multibind = { addToList(cat) { constructor(cat) } })

        assertEquals(listOf(list), graph.importFactories.map { it.type })
    }

    @Test
    fun `a module exports each of its contributions`() {
        val graph = graph(
            module = true,
            multibind = {
                addToList(cat) { constructor(cat) }
                addToSet(food) { constructor(food) }
            },
        )

        assertEquals(listOf(BindTarget.List, BindTarget.Set), graph.contributions.map { it.target })
        assertEquals(listOf(cat, food), graph.contributions.map { it.factory.type })
    }

    @Test
    fun `contributions imported from a module join the local ones`() {
        val graph = graph(
            parametrized(BuiltInTypes.List, cat),
            multibind = { addToList(cat) { constructor(cat) } },
            imports = { putContribution(BindTarget.List, null, external("spike_list_lib"), exported) },
        )

        val root = assertIs<TypeFactory.MultibindsCollection>(graph.properties.single())
        assertIs<TypeFactory.Class>(root.entries[0])
        assertEquals(external("spike_list_lib"), root.entries[1])
    }

    @Test
    fun `the same contribution imported twice counts once`() {
        val graph = graph(
            parametrized(BuiltInTypes.List, cat),
            imports = {
                putContribution(BindTarget.List, null, external("spike_list_lib"), exported)
                putContribution(BindTarget.List, null, external("spike_list_lib", origin = "OtherImpl"), exported)
            },
        )

        val root = assertIs<TypeFactory.MultibindsCollection>(graph.properties.single())
        assertEquals(1, root.entries.size)
    }

    @Test
    fun `a map key contributed locally and imported from a module fails as a duplicate key`() {
        val key = Key(Type.Simple("kotlin", "String", false), "tom", type("Name"))
        val error = assertFailsWith<DiagnosticException> {
            graph(
                parametrized(BuiltInTypes.Map, Type.Simple("kotlin", "String", false), cat),
                multibind = { addToMap(cat, key) { constructor(cat) } },
                imports = { putContribution(BindTarget.Map, key, external("spike_map_lib"), exported) },
            )
        }

        assertEquals(listOf("SPIKE-DUPLICATE-MAP-KEY"), error.diagnostics.map { it.code })
    }

    @Test
    fun `a collection with more than a hundred entries is built from concatenated segments`() {
        val names = (0 until 250).map { type("Cat$it") }
        val graph = graph(
            parametrized(BuiltInTypes.List, cat),
            multibind = { addToList(cat) { for (name in names) addFactory(cat, method(name), noArguments, false) } },
        )

        val root = assertIs<TypeFactory.Concatenation>(graph.properties.single())
        assertEquals(listOf(100, 100, 50), root.parts.map { (it as TypeFactory.MultibindsCollection).entries.size })
    }

    private val noArguments = Invocation(emptyList(), false)

    private fun graph(
        vararg properties: Type,
        module: Boolean = false,
        parameters: List<Parameter> = emptyList(),
        multibind: MultiBindingStore.Builder.() -> Unit = {},
        imports: DependencyGraph.Factory.() -> Unit = {},
        store: GraphStore.Builder.() -> Unit = {},
    ): DependencyGraph {
        val entryType = type("Entry")
        val factoryType = type("EntryFactory")
        val entry = GraphEntryPoint(
            type = entryType,
            factory = GraphEntryPoint.Factory(
                type = factoryType,
                method = Member.Method("test", "create", entryType, factoryType, parameters),
            ),
            properties = properties.mapIndexed { index, property -> Member.Property("test", "p$index", property) },
            methods = emptyList(),
            role = if (module) GraphRole.Module else GraphRole.App,
        )
        return DependencyGraph.Factory(
            entry = entry,
            root = GraphStore.Builder().apply(store).build(),
            multibinding = MultiBindingStore.Builder().apply(multibind).build(),
            imports = emptySequence(),
        ).apply(imports).create()
    }

    private fun GraphStore.Builder.constructor(type: Type, vararg dependencies: Type) = addConstructor(
        type,
        Invocation(dependencies.mapIndexed { index, dependency -> Parameter("p$index", dependency, false) }, false),
        false,
    )

    private fun method(type: Type) = Member.Method("test", type.simpleName.lowercase(), type)

    private fun external(name: String, origin: String = "ExportedImpl") =
        TypeFactory.External(cat, name, isMethod = true, origin = type(origin))

    private fun parametrized(envelope: Type, vararg arguments: Type) =
        Type.Parametrized(envelope, arguments.toList(), false)

    private fun type(name: String) = Type.Simple("test", name, false)
}
