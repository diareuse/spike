package spike.compiler.assertion

import java.io.File
import kotlin.test.assertEquals

fun Sequence<File>.assertContentEquals(other: Sequence<File>) {
    val source = sortedBy { it.path }.toList()
    val other = other.sortedBy { it.path }.toList()
    assertEquals(
        expected = source.map { it.path },
        actual = other.map { it.path }
    )

    for ((source, other) in source.zip(other).filter { (a, b) -> a.isFile && b.isFile }) {
        assertEquals(
            expected = source.readText(),
            actual = other.readText(),
            message = "File contents do not match"
        )
    }
}