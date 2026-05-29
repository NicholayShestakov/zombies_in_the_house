package game.factories

import game.cards.EntityAbility
import game.cards.ItemType
import game.testHuman
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class PlayerFactoryTest {
    @Test
    fun `createPlayers maps names to humans and grants starting items`() {
        val karateKid = testHuman(abilities = listOf(EntityAbility.START_KNIFE))
        val policeman = testHuman(abilities = listOf(EntityAbility.START_GUN))
        val runner = testHuman(plusSpeed = 1)

        val players =
            PlayerFactory().createPlayers(
                listOf("Karate", "Police", "Runner"),
                listOf(karateKid, policeman, runner),
            )

        assertEquals("Karate", players[0].name)
        assertEquals(karateKid, players[0].currentEntity)
        assertEquals(listOf(ItemType.KNIFE), players[0].inventory.map { it.itemType })
        assertEquals(listOf(ItemType.GUN), players[1].inventory.map { it.itemType })
        assertTrue(players[2].inventory.isEmpty())
    }
}
