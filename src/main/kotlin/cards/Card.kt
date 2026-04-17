package cards

abstract class Card(
    val name: String,
    private var _coordinates: Pair<Int, Int>,
) {

    private var _isFaceDown = true
    val isFaceDown: Boolean
        get() {
            return _isFaceDown
        }
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

    abstract fun use()
}