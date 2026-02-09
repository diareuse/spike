package spike.compiler.assertion

import java.io.File
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals

fun Sequence<File>.assertContentEquals(other: Sequence<File>) {
    val source = sortedBy { it.name }.toList()
    val other = other.sortedBy { it.name }.toList()
    assertEquals(
        expected = source.map { it.name },
        actual = other.map { it.name }
    )
    for (i in other.indices) {
        assertEquals(
            expected = source[i].readText(),
            actual = other[i].readText(),
            message = "File contents do not match"
        )
    }
}