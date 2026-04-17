import cards.Card

interface Inventory {

    fun addCard(card: Card)
    fun removeCard(card: Card): Boolean
    fun containsCard(card: Card): Boolean
    fun getCards(): List<Card>
}