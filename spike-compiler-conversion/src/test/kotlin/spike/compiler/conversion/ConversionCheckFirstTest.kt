package spike.compiler.conversion

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class ConversionCheckFirstTest {

    @Test
    fun `finds the problem of the first check that fails`() {
        val first = problem("SPIKE-A")
        val check = ConversionCheckFirst(listOf(passing(), failing(first), failing(problem("SPIKE-B"))))

        assertEquals(first, check.check("subject"))
    }

    @Test
    fun `finds no problem when every check passes`() {
        val check = ConversionCheckFirst(listOf(passing(), passing()))

        assertNull(check.check("subject"))
    }

    @Test
    fun `stops at the first failing check`() {
        val checked = mutableListOf<String>()
        val check = ConversionCheckFirst(
            listOf(
                ConversionCheck<String> { checked += "first"; problem("SPIKE-A") },
                ConversionCheck { checked += "second"; null },
            ),
        )

        check.check("subject")

        assertEquals(listOf("first"), checked)
    }

    private fun passing() = ConversionCheck<String> { null }

    private fun failing(problem: ConversionProblem) = ConversionCheck<String> { problem }
}
