import cards.Card

class Cell(
    val coordinates: Pair<Int, Int>,
    val width: Int,
    val height: Int,
    val conditions: List<CellCondition> = listOf<CellCondition>(),
    private var isSealedUp: Boolean = false
): Inventory {

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