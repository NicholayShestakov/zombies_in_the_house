package game.factories

import game.cards.EntityAbility
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class HumanFactoryTest {
    @Test
    fun `createDefaultShuffledHumans returns visible humans with configured abilities`() {
        val humans = HumanFactory().createDefaultShuffledHumans(MAX_PLAYER_COUNT, START_HEALTH)
        val humansByName = humans.associateBy { it.cardNameKey }

        assertEquals(MAX_PLAYER_COUNT, humans.size)
        assertTrue(humans.all { it.isHuman })
        assertTrue(humans.none { it.isFaceDown })
        assertContains(humansByName.getValue("cards.entity.human.karate_kid.name").abilities, EntityAbility.START_KNIFE)
        assertContains(humansByName.getValue("cards.entity.human.nurse.name").abilities, EntityAbility.DOUBLE_HEAL)
        assertEquals(1, humansByName.getValue("cards.entity.human.runner.name").plusSpeed)
        assertContains(humansByName.getValue("cards.entity.human.policeman.name").abilities, EntityAbility.START_GUN)
        assertEquals(START_HEALTH + 2, humansByName.getValue("cards.entity.human.athlete.name").healthPoints)
    }

    private companion object {
        const val MAX_PLAYER_COUNT = 5
        const val START_HEALTH = 3
    }
}
