package game.cards

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class CardTest {
    @Test
    fun `flip flips card`() {
        val card = TestCard()
        assertTrue(card.isFaceDown)
        card.flip()
        assertFalse(card.isFaceDown)
    }

    @Test
    fun `flip on open card does not works`() {
        val card = TestCard()
        card.flip()
        assertFalse(card.isFaceDown)
        card.flip()
        assertFalse(card.isFaceDown)
    }

    private class TestCard : Card("card.name", "card.description", true)
}
