import cards.Entity

class Player(
    val id: Int,
    val name: String,
    private var _currentEntity: Entity?
) {

    private var currentStepCount = 0
    private var isHuman = true
    private var previousCell: Cell? = null
    val currentEntity: Entity?
        get() {
            return _currentEntity
        }

    fun setCurrentEntity(entity: Entity?) {
        _currentEntity = entity
    }

}