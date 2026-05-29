package gui

import androidx.lifecycle.ViewModel
import game.actions.Action
import game.actions.ActionManager
import game.board.CellType
import game.board.Direction
import game.cards.Card
import game.cards.Entity
import game.cards.Item
import game.cards.ItemType
import game.gameControl.ReadOnlyGameContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.Locale
import java.util.ResourceBundle

private const val AUTOMATIC_ACTION_LIMIT = 100
private const val LANGUAGE_BUNDLE_PREFIX = "language_"
private const val HIDDEN_CARD_NAME_KEY = "gui.card.hidden.name"
private const val HIDDEN_CARD_DESCRIPTION_KEY = "gui.card.hidden.description"
private const val CELL_TYPE_NAME_KEY_PREFIX = "enum.cell_type."
private const val GAME_STATE_NAME_KEY_PREFIX = "enum.game_state."
private const val SPINNER_VALUE_NAME_KEY_PREFIX = "enum.spinner_value."
private const val NAME_KEY_SUFFIX = ".name"
private const val ACTIVE_PLAYER_CELL_LABEL = "А"
private const val EMPTY_CELL_LABEL = "·"
private const val HIDDEN_CARD_CELL_LABEL = "?"
private const val HUMAN_CELL_LABEL = "🙂"
private const val MONSTER_CELL_LABEL = "🧟"
private const val ZOMBIE_CELL_LABEL = "🧟"
private const val SPIDER_CELL_LABEL = "🕷"
private const val DOG_CELL_LABEL = "🐕"
private const val BOSS_CELL_LABEL = "👑"
private const val ZOMBIE_CARD_NAME_KEY = "cards.entity.monster.zombie.name"
private const val SPIDER_CARD_NAME_KEY = "cards.entity.monster.spider.name"
private const val DOG_CARD_NAME_KEY = "cards.entity.monster.dog.name"
private const val BOSS_CARD_NAME_KEY = "cards.entity.monster.boss.name"
private const val FIRST_AID_KIT_CELL_LABEL = "➕"
private const val WIN_CONDITION_CELL_LABEL = "🏆"
private const val KNIFE_CELL_LABEL = "🔪"
private const val GUN_CELL_LABEL = "🔫"
private const val GRENADE_CELL_LABEL = "💣"
private const val GRENADE_LAUNCHER_CELL_LABEL = "🚀"
private const val ITEM_CELL_LABEL = "🎒"
private const val START_CELL_LABEL = "С"
private const val FINISH_CELL_LABEL = "Ф"
private const val HUMAN_CARD_TYPE = "Человек"
private const val MONSTER_CARD_TYPE = "Монстр"
private const val WALL_TOP_TEXT = "сверху"
private const val WALL_RIGHT_TEXT = "справа"
private const val WALL_BOTTOM_TEXT = "снизу"
private const val WALL_LEFT_TEXT = "слева"

class GameViewModel(
    private val manager: ActionManager,
    private val context: ReadOnlyGameContext,
) : ViewModel() {
    private var availableActions: List<Action> = emptyList()
    private var selectedCellCoordinates: Pair<Int, Int>? = null
    private val localizationBundle =
        runCatching { ResourceBundle.getBundle(LANGUAGE_BUNDLE_PREFIX + context.localization) }.getOrNull()

    private val _uiState = MutableStateFlow(buildUiState())
    val uiState = _uiState.asStateFlow()

    init {
        applyAutomaticActions()
    }

    fun onActionClicked(actionId: Int) {
        val action = availableActions.getOrNull(actionId)
        if (action == null) {
            updateUiState("Действие больше недоступно")
            return
        }

        val message =
            runCatching { action.execute() }
                .fold(
                    onSuccess = { executed ->
                        if (executed) {
                            "Выполнено: ${action.getName()}"
                        } else {
                            "Не удалось выполнить: ${action.getName()}"
                        }
                    },
                    onFailure = { error -> error.message ?: "Действие завершилось с ошибкой" },
                )

        applyAutomaticActions(message)
    }

    fun onCellClicked(
        x: Int,
        y: Int,
    ) {
        selectedCellCoordinates = x to y
        updateUiState()
    }

    fun refresh() {
        applyAutomaticActions("Состояние обновлено")
    }

    private fun applyAutomaticActions(initialMessage: String? = null) {
        var automaticActionName: String? = null
        var message = initialMessage
        var executedCount = 0
        var shouldContinue = true

        while (shouldContinue && executedCount < AUTOMATIC_ACTION_LIMIT) {
            val action = manager.higherAvailableAutomatic
            if (action == null) {
                shouldContinue = false
            } else {
                automaticActionName = action.getName()
                val result = runCatching { action.execute() }

                if (result.isFailure) {
                    message = result.exceptionOrNull()?.message ?: "Автоматическое действие завершилось с ошибкой"
                    shouldContinue = false
                } else if (result.getOrDefault(false)) {
                    message = "Автоматически: ${action.getName()}"
                    executedCount++
                } else {
                    shouldContinue = false
                }
            }
        }

        if (executedCount == AUTOMATIC_ACTION_LIMIT) {
            message = "Остановлена цепочка автоматических действий"
        }

        updateUiState(message, automaticActionName)
    }

    private fun updateUiState(
        message: String? = null,
        automaticActionName: String? = null,
    ) {
        _uiState.update { buildUiState(message, automaticActionName) }
    }

    private fun buildUiState(
        message: String? = null,
        automaticActionName: String? = null,
    ): GameUiState {
        availableActions = manager.availableNonautomaticList
        val boardRows = buildBoardRows()

        return GameUiState(
            activePlayerName = context.activePlayerName,
            activePlayerEntityName = localize(context.activePlayerEntityNameKey),
            activePlayerHealth = context.activePlayerHealth,
            activePlayerStepCount = context.activePlayerStepCount,
            activePlayerCoordinates = context.activePlayerCoordinates,
            players = buildPlayers(),
            finalGame = buildFinalGame(),
            gameState = context.gameState,
            gameStateName = localizeEnum(GAME_STATE_NAME_KEY_PREFIX, context.gameState.name),
            spinnerValue = localizeEnum(SPINNER_VALUE_NAME_KEY_PREFIX, context.spinnerValue.name),
            winConditions = context.winConditions,
            winConditionsToWin = context.winConditionsToWin,
            inventory = context.activePlayerInventory.map(::cardToUiModel),
            boardRows = boardRows,
            selectedCell = boardRows.flatten().firstOrNull { it.isSelected },
            actions =
                availableActions.mapIndexed { index, action ->
                    GameActionUiModel(
                        id = index,
                        name = action.getName(),
                        description = action.getDescription(),
                    )
                },
            automaticActionName = automaticActionName,
            message = message,
        )
    }

    private fun buildPlayers(): List<GamePlayerUiModel> {
        return context.playersNames.mapIndexed { index, name ->
            GamePlayerUiModel(
                name = name,
                isActive = index == context.activePlayerIndex,
            )
        }
    }

    private fun buildFinalGame(): FinalGameUiModel? {
        return when {
            context.isWin ->
                FinalGameUiModel(
                    isWin = true,
                    winnerNames = context.playersOnFinishNames,
                )
            context.isLose ->
                FinalGameUiModel(
                    isWin = false,
                )
            else -> null
        }
    }

    private fun buildBoardRows(): List<List<GameBoardCellUiModel>> {
        val width = context.boardWidth
        val height = context.boardHeight
        val cells = context.boardCellsCardsAndConditions
        val selectedCoordinates = selectedCellCoordinates ?: context.activePlayerCoordinates ?: (0 to 0)

        return (height - 1 downTo 0).map { y ->
            (0 until width).map { x ->
                val cellIndex = x * height + y
                val cellContent = cells.getOrNull(cellIndex) ?: (emptyList<Card>() to emptyList<CellType>())
                val isActivePlayerCell = context.activePlayerCoordinates == x to y
                val isSelected = selectedCoordinates == x to y
                cellToUiModel(x, y, cellContent, isActivePlayerCell, isSelected)
            }
        }
    }

    private fun cellToUiModel(
        x: Int,
        y: Int,
        cellContent: Pair<List<Card>, List<CellType>>,
        isActivePlayerCell: Boolean,
        isSelected: Boolean,
    ): GameBoardCellUiModel {
        val coordinates = x to y
        val cards = cellContent.first.map(::cardToUiModel)
        val conditions = cellContent.second.map(::cellConditionToText)
        val wallDirections = buildWallDirections(coordinates)

        return GameBoardCellUiModel(
            x = x,
            y = y,
            shortText = buildCellShortText(cellContent, isActivePlayerCell),
            topCardText = cellContent.first.lastOrNull()?.toCellLabel(),
            cards = cards,
            conditions = conditions,
            walls = wallDirections.map(::wallDirectionToText),
            hasTopWall = Direction.UP in wallDirections,
            hasRightWall = Direction.RIGHT in wallDirections,
            hasBottomWall = Direction.DOWN in wallDirections,
            hasLeftWall = Direction.LEFT in wallDirections,
            isActivePlayerCell = isActivePlayerCell,
            isSelected = isSelected,
        )
    }

    private fun buildWallDirections(coordinates: Pair<Int, Int>): List<Direction> {
        return Direction.entries.filter { direction ->
            context.boardWalls.any { wall -> wall.matches(coordinates, direction) }
        }
    }

    private fun Pair<Pair<Int, Int>, Pair<Int, Int>>.matches(
        coordinates: Pair<Int, Int>,
        direction: Direction,
    ): Boolean {
        val neighbourCoordinates =
            coordinates.first + direction.delta.first to coordinates.second + direction.delta.second
        return first == coordinates && second == neighbourCoordinates ||
            first == neighbourCoordinates && second == coordinates
    }

    private fun wallDirectionToText(direction: Direction): String {
        return when (direction) {
            Direction.UP -> WALL_TOP_TEXT
            Direction.RIGHT -> WALL_RIGHT_TEXT
            Direction.DOWN -> WALL_BOTTOM_TEXT
            Direction.LEFT -> WALL_LEFT_TEXT
        }
    }

    private fun buildCellShortText(
        cellContent: Pair<List<Card>, List<CellType>>,
        isActivePlayerCell: Boolean,
    ): String {
        val labels = mutableListOf<String>()
        val conditions = cellContent.second

        if (isActivePlayerCell) {
            labels.add(ACTIVE_PLAYER_CELL_LABEL)
        }
        if (CellType.START in conditions) {
            labels.add(START_CELL_LABEL)
        }
        if (CellType.FINISH in conditions) {
            labels.add(FINISH_CELL_LABEL)
        }
        return labels.ifEmpty { listOf(EMPTY_CELL_LABEL) }.joinToString(separator = " ")
    }

    private fun Card.toCellLabel(): String {
        return if (isFaceDown) {
            HIDDEN_CARD_CELL_LABEL
        } else {
            when (this) {
                is Entity -> toCellLabel()
                is Item -> itemType.toCellLabel()
                else -> ITEM_CELL_LABEL
            }
        }
    }

    private fun Entity.toCellLabel(): String {
        return if (isHuman) {
            HUMAN_CELL_LABEL
        } else {
            when (cardNameKey) {
                ZOMBIE_CARD_NAME_KEY -> ZOMBIE_CELL_LABEL
                SPIDER_CARD_NAME_KEY -> SPIDER_CELL_LABEL
                DOG_CARD_NAME_KEY -> DOG_CELL_LABEL
                BOSS_CARD_NAME_KEY -> BOSS_CELL_LABEL
                else -> MONSTER_CELL_LABEL
            }
        }
    }

    private fun ItemType.toCellLabel(): String {
        return when (this) {
            ItemType.FIRST_AID_KIT -> FIRST_AID_KIT_CELL_LABEL
            ItemType.WIN_CONDITION -> WIN_CONDITION_CELL_LABEL
            ItemType.KNIFE -> KNIFE_CELL_LABEL
            ItemType.GUN -> GUN_CELL_LABEL
            ItemType.GRENADE -> GRENADE_CELL_LABEL
            ItemType.GRENADE_LAUNCHER -> GRENADE_LAUNCHER_CELL_LABEL
        }
    }

    private fun cardToUiModel(card: Card): GameCardUiModel {
        val isFaceDown = card.isFaceDown
        val name = localize(if (isFaceDown) HIDDEN_CARD_NAME_KEY else card.cardNameKey)
        val description = localize(if (isFaceDown) HIDDEN_CARD_DESCRIPTION_KEY else card.cardDescriptionKey)

        return GameCardUiModel(
            name = name,
            description = description,
            details = if (isFaceDown) emptyList() else cardDetails(card),
            isFaceDown = isFaceDown,
        )
    }

    private fun cardDetails(card: Card): List<String> {
        return when (card) {
            is Entity -> {
                val type = if (card.isHuman) HUMAN_CARD_TYPE else MONSTER_CARD_TYPE
                listOf("Тип: $type", "Здоровье: ${card.healthPoints}")
            }
            else -> emptyList()
        }
    }

    private fun cellConditionToText(condition: CellType): String {
        return localizeEnum(CELL_TYPE_NAME_KEY_PREFIX, condition.name)
    }

    private fun localizeEnum(
        prefix: String,
        enumName: String,
    ): String = localize(prefix + enumName.lowercase(Locale.ROOT) + NAME_KEY_SUFFIX)

    private fun localize(key: String): String =
        localizationBundle?.let { bundle -> runCatching { bundle.getString(key) }.getOrNull() } ?: key
}
