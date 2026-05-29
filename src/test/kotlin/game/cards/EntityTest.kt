package game.cards

import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class EntityTest {
    @Test
    fun `entity stores combat health ability and movement fields`() {
        val entity =
            Entity(
                "human.name",
                "human.description",
                isHuman = true,
                canDamagedBy = listOf(DamageType.FANGS),
                healthPoints = 5,
                abilities = listOf(EntityAbility.DOUBLE_HEAL),
                plusSpeed = 1,
                isFaceDown = false,
            )

        assertEquals("human.name", entity.cardNameKey)
        assertTrue(entity.isHuman)
        assertContains(entity.canDamagedBy, DamageType.FANGS)
        assertEquals(5, entity.healthPoints)
        assertContains(entity.abilities, EntityAbility.DOUBLE_HEAL)
        assertEquals(1, entity.plusSpeed)
        assertFalse(entity.isFaceDown)
    }
}
