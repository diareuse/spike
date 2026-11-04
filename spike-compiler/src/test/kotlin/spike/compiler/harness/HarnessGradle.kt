package spike.compiler.harness

import org.gradle.testkit.runner.BuildResult
import org.gradle.testkit.runner.TaskOutcome
import spike.compiler.assertion.assertContentEquals
import kotlin.test.assertContains
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

    protected fun assertBuilds(
        name: String,
        template: String? = null,
        tasks: List<String> = listOf(":run"),
        arguments: List<String> = emptyList()
    ) {
        assertTasksSucceeded(FixtureProject(name, template).build(tasks, arguments), tasks)
    }

    protected fun assertRebuildsAfterEdit(
        name: String,
        path: String,
        oldText: String,
        newText: String,
        tasks: List<String> = listOf(":run")
    ) {
        val project = FixtureProject(name, null)
        assertTasksSucceeded(project.build(tasks), tasks)
        project.replace(path, oldText, newText)
        assertTasksSucceeded(project.build(tasks), tasks)
    }

    protected fun warmBuilds(name: String, arguments: List<String> = emptyList()): WarmBuilds =
        WarmBuilds(FixtureProject(name, null), arguments)

    protected class WarmBuilds(private val project: FixtureProject, private val arguments: List<String>) {
        init {
            if (project.directory.resolve("build.gradle.kts").readText().contains("compiler.jar")) project.copyCompilerJar()
        }

        fun build(tasks: List<String> = listOf(":kspKotlin")): String =
            project.build(tasks, arguments + "--info").output

        fun edit(path: String, oldText: String, newText: String) = project.replace(path, oldText, newText)

        fun changeCompilerJar() = project.changeCompilerJar()
    }

    protected fun assertBuildFails(name: String, tasks: List<String>, vararg messages: String) {
        val output = FixtureProject(name, null).buildAndFail(tasks).output
        for (message in messages) assertContains(output, message)
    }

    private fun assertTasksSucceeded(result: BuildResult, tasks: List<String>) {
        for (task in tasks.filter { it.startsWith(":") }) {
            assertEquals(TaskOutcome.SUCCESS, result.task(task)?.outcome, "$task did not run")
        }
    }

}
