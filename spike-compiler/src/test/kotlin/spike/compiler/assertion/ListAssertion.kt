package spike.compiler.assertion

import java.io.File
import kotlin.test.assertEquals

fun assertContentEquals(expected: File, actual: File) {
    val expectedFiles = expected.listFiles()?.sortedBy { it.name }.orEmpty()
    val actualFiles = actual.listFiles()?.sortedBy { it.name }.orEmpty()

    assertEquals(expectedFiles.size, actualFiles.size, "File directory")

    for (i in expectedFiles.indices) {
        val expectedFile = expectedFiles[i]
        val actualFile = actualFiles[i]

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
