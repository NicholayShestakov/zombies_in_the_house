package game.cards.entities

import game.Board
import game.cards.Card

abstract class Entity(
    cardNameKey: String,
    cardDescriptionKey: String,
    val isHuman: Boolean,
    val canDamagedBy: List<DamageType>,
    private var _healthPoints: Int,
    val abilities: List<EntityAbility>,
    val plusSpeed: Int = 0
) : Card(cardNameKey, cardDescriptionKey) {

    val isAlive: Boolean
        get() {
            return _healthPoints > 0
        }

    val healthPoints: Int
        get() = _healthPoints

    fun addHealth(count: Int) {
        _healthPoints += count
    }
}