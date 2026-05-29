package game.gameControl.contextManagers

import game.SpinnerValue
import org.junit.jupiter.api.BeforeEach
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals

class SpinnerManagerTest {
    private lateinit var manager: SpinnerManager

    @BeforeEach
    fun initializeManager() {
        manager = SpinnerManager()
    }

    @Test
    fun `spinner manager gets correct values`() {
        repeat(100) {
            manager.spin()
            assertContains(SpinnerValue.entries, manager.spinnerValue)
        }
    }

    @Test
    fun `spinner manager resets value correctly`() {
        repeat(20) {
            manager.spin()
            manager.resetForBattle()
            assertEquals(SpinnerValue.FANGS, manager.spinnerValue)
        }
    }
}
