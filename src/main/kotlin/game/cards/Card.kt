package game.cards

abstract class Card(
    val cardNameKey: String,
    val cardDescriptionKey: String,
    private var privateIsFaceDown: Boolean,
) {
    val isFaceDown: Boolean
        get() {
            return privateIsFaceDown
        }

    fun flip() {
        privateIsFaceDown = false
    }
}
