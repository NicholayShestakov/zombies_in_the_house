package cards

import Inventory

abstract class Human(
    cardName: String,
    cardCoordinates: Pair<Int, Int>,
) : Entity(cardName, cardCoordinates), Inventory {

    private val inventory = mutableListOf<Card>()

    override fun addCard(card: Card) {
        inventory.add(card)
    }

    override fun removeCard(card: Card): Boolean {
        return inventory.remove(card)
    }

    override fun containsCard(card: Card): Boolean {
        return inventory.contains(card)
    }

    override fun getCards(): List<Card> {
        return inventory.toList()
    }
}