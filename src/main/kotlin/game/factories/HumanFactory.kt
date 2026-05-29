package game.factories

import game.cards.DamageType
import game.cards.Entity
import game.cards.EntityAbility
import kotlin.random.Random

class HumanFactory {
    fun createDefaultShuffledHumans(
        playerCount: Int,
        startHealth: Int,
        seed: Int? = null,
    ): List<Entity> {
        val humans =
            listOf(
                Entity(
                    "cards.entity.human.karate_kid.name",
                    "cards.entity.human.karate_kid.description",
                    true,
                    listOf(DamageType.FANGS),
                    startHealth,
                    listOf(EntityAbility.START_KNIFE),
                    isFaceDown = false,
                ),
                Entity(
                    "cards.entity.human.nurse.name",
                    "cards.entity.human.nurse.description",
                    true,
                    listOf(DamageType.FANGS),
                    startHealth,
                    listOf(EntityAbility.DOUBLE_HEAL),
                    isFaceDown = false,
                ),
                Entity(
                    "cards.entity.human.runner.name",
                    "cards.entity.human.runner.description",
                    true,
                    listOf(DamageType.FANGS),
                    startHealth,
                    plusSpeed = 1,
                    isFaceDown = false,
                ),
                Entity(
                    "cards.entity.human.policeman.name",
                    "cards.entity.human.policeman.description",
                    true,
                    listOf(DamageType.FANGS),
                    startHealth,
                    listOf(EntityAbility.START_GUN),
                    isFaceDown = false,
                ),
                Entity(
                    "cards.entity.human.athlete.name",
                    "cards.entity.human.athlete.description",
                    true,
                    listOf(DamageType.FANGS),
                    startHealth + 2,
                    isFaceDown = false,
                ),
            )

        return humans.shuffled(seed?.let { Random(seed) } ?: Random).subList(0, playerCount)
    }
}
