package spike.compiler.harness

import org.gradle.testkit.runner.BuildResult
import org.gradle.testkit.runner.BuildTask
import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome
import java.io.File
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

    protected fun assertSuccess(task: BuildTask) {
        //assertEquals(TaskOutcome.SUCCESS, task.outcome)
    }
    protected fun assertFailure(task: BuildTask) = assertEquals(TaskOutcome.FAILED, task.outcome)

    protected fun test(testcase: String) = _test(
        testcase = testcase,
        verify = { result, files ->
            assertEquals(TaskOutcome.SUCCESS, result.outcome)
            check(files.count() > 0) { "No output files found" }
            val url = TestHarness::class.java.classLoader.getResource("$testcase/fixtures")
            check(url?.protocol == "file") { "Testcase $testcase fixtures could not be found" }
            val file = File(url.toURI().path)
            val actual = files.sortedBy { it.name }.toList()
            val expected = file.walk().filter { it.isFile }.sortedBy { it.name }.toList()
            assertEquals(
                expected.map { it.name },
                actual.map { it.name },
                "File names do not match"
            )
            for (i in actual.indices) {
                assertEquals(
                    expected[i].readText(),
                    actual[i].readText(),
                    "File contents do not match"
                )
            }
        }
    )

    protected fun testFail(testcase: String) = _testFail(
        testcase = testcase,
        verify = { result, _ -> assertEquals(TaskOutcome.FAILED, result.outcome) }
    )

    private fun _test(
        testcase: String,
        verify: (BuildTask, Sequence<File>) -> Unit
    ) = test(
        label = testcase,
        prepare = { dir ->
            val url = TestHarness::class.java.classLoader.getResource("$testcase/project")
            check(url?.protocol == "file") { "Testcase $testcase not found" }
            File(url.toURI().path).copyRecursively(dir.resolve("src/main/kotlin"))
        },
        verify = verify
    )

    private fun _testFail(
        testcase: String,
        verify: (BuildTask, Sequence<File>) -> Unit
    ) = testFail(
        label = testcase,
        prepare = { dir ->
            val url = TestHarness::class.java.classLoader.getResource("$testcase/project")
            check(url?.protocol == "file") { "Testcase $testcase not found" }
            File(url.toURI().path).copyRecursively(dir.resolve("src/main/kotlin"))
        },
        verify = verify
    )


    protected fun test(
        label: String,
        prepare: (File) -> Unit,
        verify: (result: BuildTask, outputFiles: Sequence<File>) -> Unit
    ) = _test(label, prepare, { it.build() }, verify)

    protected fun testFail(
        label: String,
        prepare: (File) -> Unit,
        verify: (result: BuildTask, outputFiles: Sequence<File>) -> Unit
    ) = _test(label, prepare, { it.buildAndFail() }, verify)

    private fun _test(
        label: String,
        prepare: (File) -> Unit,
        build: (GradleRunner) -> BuildResult,
        verify: (result: BuildTask, outputFiles: Sequence<File>) -> Unit
    ) {
        val projectRoot = File("build/tmp/$label").apply {
            deleteRecursively()
            mkdirs()
        }
        projectRoot.resolve("build.gradle.kts").writeText(generateBuildScript())
        projectRoot.resolve("settings.gradle").writeText("")
        prepare(projectRoot)
        val runner = GradleRunner.create()
            .withProjectDir(projectRoot)
            .withArguments("kspKotlin", "compileKotlin", "--stacktrace")
            .forwardOutput()
        val result = build(runner)

        verify(
            result.task(":kspKotlin")!!,
            projectRoot.resolve("build/generated/ksp/main/kotlin").walk().filter { it.isFile }
        )
        val compileKotlin = result.task(":compileKotlin")
        if (compileKotlin != null)
            assertEquals(
                TaskOutcome.SUCCESS,
                compileKotlin.outcome,
                "Verification succeeded, but compilation failed:"
            )
    }

    private fun generateBuildScript(): String {
        val classPath = System.getProperty("java.class.path")
        val dependencies = listOf("kotlinpoet", "ksp", "")

        val classpathFiles = classPath
            .splitToSequence(File.pathSeparator)
            .filter { it.contains("spike-") }
            .map { File(it).absolutePath.replace("\\", "/") }
            .joinToString { "\"$it\"" }

        val compilerClasspathFiles = classPath
            .splitToSequence(File.pathSeparator)
            .filter { it.contains("ksp") || it.contains("-compiler") || it.contains("spike") }
            .map { File(it).absolutePath.replace("\\", "/") }
            .joinToString { "\"$it\"" }

        println(classpathFiles)

        return """
            plugins {
                kotlin("jvm") version "2.2.21"
                id("com.google.devtools.ksp") version "2.3.4"
            }
            
            repositories { mavenCentral() }

            dependencies {
                implementation(kotlin("stdlib"))
                implementation(files($classpathFiles))
                ksp(files($compilerClasspathFiles))
            }
        """.trimIndent()
    }

}