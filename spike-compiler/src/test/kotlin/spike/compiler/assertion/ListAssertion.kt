package spike.compiler.assertion

import java.io.File
import kotlin.test.assertEquals

fun assertContentEquals(expected: File, actual: File) {
    val expectedValues = expected.listFiles()
        ?.sortedBy { it.name }
        ?.filterNot { it.isDirectory && it.listFiles().isEmpty() }
        .orEmpty()
    val actualValues = actual.listFiles()
        ?.sortedBy { it.name }
        ?.filterNot { it.isDirectory && it.listFiles().isEmpty() }
        .orEmpty()

    assertEquals(expectedValues.size, actualValues.size, "File count doesn't match")

    for (i in expectedValues.indices) {
        val expectedFile = expectedValues[i]
        val actualFile = actualValues[i]

        assertEquals(expectedFile.name, actualFile.name)

        if (expectedFile.isFile && actualFile.isFile) {
            compareFileContents(expectedFile, actualFile)
        } else if (expectedFile.isDirectory && actualFile.isDirectory) {
            assertContentEquals(expectedFile, actualFile)
        } else {
            throw AssertionError("$expectedFile was not the same type as $actualFile")
        }
    }
}

private fun compareFileContents(
    expectedFile: File,
    actualFile: File
) = assertEquals(
    expected = expectedFile.readText(),
    actual = actualFile.readText(),
    message = "Compared $expectedFile with $actualFile, they don't match"
)
