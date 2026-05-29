package game

import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals

class SpinnerTest {
    @Test
    fun `getValue without seed returns declared spinner value`() {
        repeat(100) {
            assertContains(SpinnerValue.entries, Spinner.getValue())
        }
    }

    @Test
    fun `seeds in fixtures works correctly`() {
        assertEquals(SpinnerValue.ESCAPE, Spinner.getValue(SPINNER_ESCAPE_SEED))
        assertEquals(SpinnerValue.FANGS, Spinner.getValue(SPINNER_FANGS_SEED))
        assertEquals(SpinnerValue.BLADES, Spinner.getValue(SPINNER_BLADES_SEED))
        assertEquals(SpinnerValue.AIM, Spinner.getValue(SPINNER_AIM_SEED))
    }
}
