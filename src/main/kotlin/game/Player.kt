import cards.Entity

class Player(
    val name: String
) {

    private var currentStepCount = 0
    private var isHuman = true
    private var _previousCell: Cell? = null
    val previousCell: Cell?
        get() {
            return _previousCell
        }
    private var _currentEntity: Entity? = null
    var currentEntity: Entity?
        get() {
            return _currentEntity
        }
        set(newEntity: Entity?) {
            _currentEntity = newEntity
        }
}