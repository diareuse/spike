package spike.compiler.graph

import kotlin.test.Test
import kotlin.test.assertEquals

class GraphRoleTest {

    private val missing = listOf(TypeFactory.Imported(Type.Simple("test", "Api", false), "api", "test.Api"))
    private val exported = listOf(
        ExportedContribution(
            spike.BindTarget.Set,
            null,
            TypeFactory.Class(Type.Simple("test", "Api", false), Invocation(emptyList(), false), false, emptyList()),
            "name",
        ),
    )

    @Test
    fun `an app cannot leave a type unbound`() = assertEquals(missing, GraphRole.App.unresolved(missing))

    @Test
    fun `a module leaves unbound types to the app`() = assertEquals(emptyList(), GraphRole.Module.unresolved(missing))

    @Test
    fun `a module exports its contributions`() = assertEquals(exported, GraphRole.Module.contributions { exported })

    @Test
    fun `an app exports nothing`() = assertEquals(emptyList(), GraphRole.App.contributions { exported })
}
