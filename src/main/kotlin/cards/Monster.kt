package cards

class Monster(
    cardName: String,
    cardCoordinates: Pair<Int, Int>,
    val canKilledBy: List<Card>,
    val plusSpeed: Int = 0
) : Entity(cardName, cardCoordinates) {

    override val canBeUsed: Boolean
        get() = false

    override fun use() {
        return
    }
}