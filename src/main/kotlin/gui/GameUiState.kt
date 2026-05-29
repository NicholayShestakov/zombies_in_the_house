package gui

import game.gameControl.GameState

data class GameUiState(
    val activePlayerName: String = "",
    val activePlayerEntityName: String = "",
    val activePlayerHealth: Int = 0,
    val activePlayerStepCount: Int = 0,
    val activePlayerCoordinates: Pair<Int, Int>? = null,
    val players: List<GamePlayerUiModel> = emptyList(),
    val finalGame: FinalGameUiModel? = null,
    val gameState: GameState = GameState.STEP_SPINNER_SPINNING,
    val gameStateName: String = "",
    val spinnerValue: String = "",
    val winConditions: Int = 0,
    val winConditionsToWin: Int = 0,
    val inventory: List<GameCardUiModel> = emptyList(),
    val boardRows: List<List<GameBoardCellUiModel>> = emptyList(),
    val selectedCell: GameBoardCellUiModel? = null,
    val actions: List<GameActionUiModel> = emptyList(),
    val automaticActionName: String? = null,
    val message: String? = null,
)

data class GamePlayerUiModel(
    val name: String,
    val isActive: Boolean,
)

data class FinalGameUiModel(
    val isWin: Boolean,
    val winnerNames: List<String> = emptyList(),
)

data class GameActionUiModel(
    val id: Int,
    val name: String,
    val description: String,
)

data class GameCardUiModel(
    val name: String,
    val description: String,
    val details: List<String> = emptyList(),
    val isFaceDown: Boolean = false,
)

data class GameBoardCellUiModel(
    val x: Int,
    val y: Int,
    val shortText: String,
    val topCardText: String?,
    val cards: List<GameCardUiModel>,
    val conditions: List<String>,
    val walls: List<String>,
    val hasTopWall: Boolean,
    val hasRightWall: Boolean,
    val hasBottomWall: Boolean,
    val hasLeftWall: Boolean,
    val isActivePlayerCell: Boolean,
    val isSelected: Boolean,
)
