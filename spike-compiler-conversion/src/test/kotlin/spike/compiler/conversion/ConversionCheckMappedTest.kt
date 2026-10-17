package spike.compiler.conversion

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class ConversionCheckMappedTest {

    @Test
    fun `checks what the subject maps to`() {
        val problem = problem("SPIKE-LENGTH")
        val check = ConversionCheckMapped<String, Int>({ it.length }, { if (it > 3) problem else null })

        assertEquals(problem, check.check("long"))
    }

    @Test
    fun `leaves a subject that maps to nothing to other checks`() {
        val check = ConversionCheckMapped<String, Int>({ null }, { problem("SPIKE-ANY") })

        assertNull(check.check("subject"))
    }
}
