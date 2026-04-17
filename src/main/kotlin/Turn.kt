class Turn(
    val context: Context
) {

    private var turnNumber = 1
    private var _currentPlayer: Player = context.playerList[0]
    val currentPlayer: Player
        get() {
            return _currentPlayer
        }
    val currentCoordinates: Pair<Int, Int>
        get() {
            return currentPlayer.currentEntity?.coordinates ?: Pair(-1, -1)
        }
    val currentCell: Cell?
        get() {
            return context.board.getCell(currentCoordinates)
        }

    fun startTurn(): TurnStep {
        return TurnStep(context)
    }

    fun nextTurn() {
        _currentPlayer = context.playerList[turnNumber % context.playerList.size]
        turnNumber++
    }
}