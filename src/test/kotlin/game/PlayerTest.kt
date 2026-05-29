package game

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class PlayerTest {
    @Test
    fun `playsAsHuman is true for alive human entity`() {
        val human = testHuman()
        val player = Player("Alice", human)

        assertTrue(player.playsAsHuman)
    }

    @Test
    fun `playsAsHuman is false for dead human entity`() {
        val human = testHuman(healthPoints = 0)
        val player = Player("Alice", human)

        assertFalse(player.playsAsHuman)
    }

    @Test
    fun `playsAsHuman is false for monster entity`() {
        val human = testMonster()
        val player = Player("Alice", human)

        assertFalse(player.playsAsHuman)
    }
}
