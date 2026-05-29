package cards

abstract class Card(
    val name: String
) {

    private var _isFaceDown = true
    val isFaceDown: Boolean
        get() {
            return _isFaceDown
        }
    private var _coordinates = Pair(-1, -1)
    val coordinates: Pair<Int, Int>
        get() {
            return _coordinates
        }

    fun flip(): Boolean {
        if (_isFaceDown) {
            _isFaceDown = false
            return true
        }
        return false
    }
}