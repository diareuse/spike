package spike.compiler.harness

import spike.compiler.assertion.assertContentEquals

abstract class GradleHarness {

    protected fun assertMatchesGolden(
        name: String,
        template: Template,
        tasks: List<String> = listOf(":run"),
        modules: List<String> = listOf("")
    ) {
        val project = FixtureProject(name, template)
        project.build(tasks)
        for (module in modules) {
            assertContentEquals(project.golden.resolve(module), project.generatedSources(module))
        }
    }

    protected fun assertBuilds(
        name: String,
        template: Template,
        tasks: List<String> = listOf(":run")
    ) {
        FixtureProject(name, template).build(tasks)
    }

}
