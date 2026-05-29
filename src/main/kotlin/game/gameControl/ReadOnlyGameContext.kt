package game.gameControl

import game.SpinnerValue
import game.board.CellType
import game.cards.Card
import game.cards.Item

class ReadOnlyGameContext(private val context: GameContext) {
    val gameState: GameState get() = context.gameState
    val localization: String get() = context.localization
    val playersNames: List<String> get() = context.playersNames
    val playersOnFinishNames: List<String> get() = context.playersOnFinishNames
    val isWin: Boolean get() = context.isWin
    val isLose: Boolean get() = context.isLose
    val activePlayerIndex: Int get() = context.activePlayerIndex
    val activePlayerName: String get() = context.activePlayerName
    val activePlayerEntityNameKey: String get() = context.activePlayerEntityNameKey
    val activePlayerInventory: List<Item> get() = context.activePlayerInventoryList
    val activePlayerHealth: Int get() = context.activePlayerHealth
    val activePlayerStepCount: Int get() = context.activePlayerStepCount
    val activePlayerCoordinates: Pair<Int, Int>? get() = runCatching { context.activePlayerCoordinates }.getOrNull()
    val winConditionsToWin: Int get() = context.winConditionsToWin
    val winConditions: Int get() = context.winConditions
    val spinnerValue: SpinnerValue get() = context.spinnerValue
    val boardWidth: Int get() = context.boardWidth
    val boardHeight: Int get() = context.boardHeight
    val boardCellsCardsAndConditions: List<Pair<List<Card>, List<CellType>>>
        get() = context.boardCellsCardsAndConditions
    val boardWalls: List<Pair<Pair<Int, Int>, Pair<Int, Int>>>
        get() = context.boardWalls
}
