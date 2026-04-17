package cards

import Direction

abstract class Entity(
    cardName: String,
    cardCoordinates: Pair<Int, Int>,
    private var healthPoints: Int = 1
) : Card(cardName, cardCoordinates) {

    val isDead: Boolean
        get() {
            return healthPoints == 0
        }

    fun addHealth(count: Int) {
        healthPoints += count
        if (healthPoints < 0) {
            healthPoints = 0
        }
    }

    fun getHealth(): Int {
        return healthPoints
    }
}