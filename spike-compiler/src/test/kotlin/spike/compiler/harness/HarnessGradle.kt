package spike.compiler.harness

import org.gradle.testkit.runner.BuildResult
import org.gradle.testkit.runner.TaskOutcome
import spike.compiler.assertion.assertContentEquals
import kotlin.test.assertEquals

abstract class HarnessGradle {

    protected fun assertMatchesGolden(
        name: String,
        template: String? = null,
        tasks: List<String> = listOf(":run"),
        modules: List<String> = listOf("")
    ) {
        val project = FixtureProject(name, template)
        assertTasksSucceeded(project.build(tasks), tasks)
        for (module in modules) {
            assertContentEquals(project.golden.resolve(module), project.generatedSources(module))
        }
    }

    protected fun assertBuilds(name: String, template: String? = null, tasks: List<String> = listOf(":run")) {
        assertTasksSucceeded(FixtureProject(name, template).build(tasks), tasks)
    }

    private fun assertTasksSucceeded(result: BuildResult, tasks: List<String>) {
        for (task in tasks.filter { it.startsWith(":") }) {
            assertEquals(TaskOutcome.SUCCESS, result.task(task)?.outcome, "$task did not run")
        }
    }

}
