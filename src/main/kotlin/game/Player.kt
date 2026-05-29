package game

import game.cards.Entity
import game.cards.Item

class Player(
    val name: String,
    var currentEntity: Entity,
) {
    val inventory: MutableList<Item> = mutableListOf()
    val playsAsHuman: Boolean get() = currentEntity.isHuman && currentEntity.healthPoints > 0
}
