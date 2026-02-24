package spike.compiler.harness

import org.gradle.testkit.runner.BuildResult
import org.gradle.testkit.runner.BuildTask
import org.gradle.testkit.runner.TaskOutcome
import java.io.File
import kotlin.test.assertContains
import kotlin.test.assertEquals

abstract class TestHarness {

    protected fun runTest(
        label: String,
        prepare: GradleTestProject.Builder.() -> GradleTestProject,
        test: GradleTestProject.() -> BuildResult,
        verify: GradleTestProject.Context.(BuildResult) -> Unit = {}
    ) {
        val resourcesDir = File("src/test/resources")
        val projectRoot = File("build/tmp/projects").resolve(label)
        val builder = GradleTestProject.Builder(projectRoot)
            .cloneProjectDir(resourcesDir.resolve("$label/project"))
            .useFixturesDir(resourcesDir.resolve("$label/fixtures"))
        val project = builder.prepare()
        val result = project.test()
        project.context.verify(result)
    }

    protected fun assertSuccess(task: BuildTask) =
        assertContains(listOf(TaskOutcome.SUCCESS, TaskOutcome.UP_TO_DATE), task.outcome)

    protected fun assertFailure(task: BuildTask) = assertEquals(TaskOutcome.FAILED, task.outcome)

}