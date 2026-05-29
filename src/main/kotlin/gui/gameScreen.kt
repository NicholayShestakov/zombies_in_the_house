@file:Suppress("ktlint:standard:filename")

package gui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private const val LEFT_PANEL_WIDTH = 480
private const val RIGHT_PANEL_WIDTH = 540
private const val PANEL_SPACING = 16
private const val SECTION_SPACING = 16
private const val CONTENT_SPACING = 8
private const val ACTION_BUTTON_HEIGHT = 48
private const val PLAYERS_LIST_HEIGHT = 120
private const val ACTIONS_LIST_HEIGHT = 260
private const val BOARD_CELL_SIZE = 38
private const val BOARD_CELL_PADDING = 2
private const val BOARD_CELL_BORDER_WIDTH = 1
private const val WALL_THICKNESS = 4
private const val BOARD_GRID_SPACING = 0
private const val BOARD_CELL_FONT_SIZE = 10
private const val BOARD_TOP_CARD_FONT_SIZE = 16
private const val CARD_PADDING = 16
private const val SMALL_SPACER_HEIGHT = 8
private const val NO_VALUE = "—"
private const val EMPTY_LIST_TEXT = "нет"
private const val ACTIVE_PLAYER_TEXT = "активный"

private val emptyCellColor = Color(0xFFF7F7F7)
private val cardCellColor = Color(0xFFFFF3E0)
private val conditionCellColor = Color(0xFFE8F5E9)
private val activeCellColor = Color(0xFFE3F2FD)
private val selectedCellColor = Color(0xFFFFECB3)
private val boardCellBorderColor = Color(0xFF9E9E9E)
private val wallColor = Color(0xFF212121)

@Composable
fun gameScreen(
    gameViewModel: GameViewModel,
    onGameFinished: (FinalGameUiModel) -> Unit,
) {
    val uiState by gameViewModel.uiState.collectAsState()

    LaunchedEffect(uiState.finalGame) {
        uiState.finalGame?.let(onGameFinished)
    }

    Box(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        contentAlignment = Alignment.TopCenter,
    ) {
        gameContent(uiState, gameViewModel)
    }
}

@Composable
private fun gameContent(
    uiState: GameUiState,
    gameViewModel: GameViewModel,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(PANEL_SPACING.dp),
        verticalAlignment = Alignment.Top,
    ) {
        leftPanel(uiState, gameViewModel)
        rightPanel(uiState, gameViewModel::onCellClicked)
    }
}

@Composable
private fun leftPanel(
    uiState: GameUiState,
    gameViewModel: GameViewModel,
) {
    LazyColumn(
        modifier = Modifier.width(LEFT_PANEL_WIDTH.dp).fillMaxHeight(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(SECTION_SPACING.dp),
    ) {
        item {
            Text("Zombies in the house", style = MaterialTheme.typography.h5)
        }

        item {
            gameStatusCard(uiState)
        }

        item {
            inventoryCard(uiState.inventory)
        }

        item {
            gameProgressCard(uiState)
        }

        if (uiState.automaticActionName != null || uiState.message != null) {
            item {
                messageCard(uiState)
            }
        }

        item {
            actionsCard(
                actions = uiState.actions,
                onActionClick = gameViewModel::onActionClicked,
            )
        }

        item {
            refreshButton(gameViewModel::refresh)
        }
    }
}

@Composable
private fun rightPanel(
    uiState: GameUiState,
    onCellClick: (Int, Int) -> Unit,
) {
    Column(
        modifier = Modifier.width(RIGHT_PANEL_WIDTH.dp),
        verticalArrangement = Arrangement.spacedBy(SECTION_SPACING.dp),
    ) {
        boardCard(
            uiState = uiState,
            onCellClick = onCellClick,
            modifier = Modifier.fillMaxWidth(),
        )

        selectedCellCard(uiState.selectedCell)

        playersCard(uiState.players)
    }
}

@Composable
private fun gameStatusCard(uiState: GameUiState) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(CARD_PADDING.dp),
            verticalArrangement = Arrangement.spacedBy(CONTENT_SPACING.dp),
        ) {
            Text("Активный игрок", style = MaterialTheme.typography.subtitle1)
            infoRow("Имя", uiState.activePlayerName)
            infoRow("Играет за", uiState.activePlayerEntityName)
            infoRow("Здоровье", uiState.activePlayerHealth.toString())
            infoRow("Шаги", uiState.activePlayerStepCount.toString())
            infoRow("Координаты", uiState.activePlayerCoordinates?.formatCoordinates() ?: NO_VALUE)
        }
    }
}

@Composable
private fun gameProgressCard(uiState: GameUiState) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(CARD_PADDING.dp),
            verticalArrangement = Arrangement.spacedBy(CONTENT_SPACING.dp),
        ) {
            Text("Состояние игры", style = MaterialTheme.typography.subtitle1)
            infoRow("Состояние", uiState.gameStateName)
            infoRow("Спиннер", uiState.spinnerValue)
            infoRow("Победные предметы", "${uiState.winConditions}/${uiState.winConditionsToWin}")
        }
    }
}

@Composable
private fun playersCard(players: List<GamePlayerUiModel>) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(CARD_PADDING.dp),
            verticalArrangement = Arrangement.spacedBy(CONTENT_SPACING.dp),
        ) {
            Text("Игроки", style = MaterialTheme.typography.subtitle1)
            LazyColumn(
                modifier = Modifier.fillMaxWidth().height(PLAYERS_LIST_HEIGHT.dp),
                verticalArrangement = Arrangement.spacedBy(CONTENT_SPACING.dp),
            ) {
                if (players.isEmpty()) {
                    item {
                        Text("Игроков нет", style = MaterialTheme.typography.body2)
                    }
                } else {
                    items(players) { player ->
                        playerRow(player)
                    }
                }
            }
        }
    }
}

@Composable
private fun playerRow(player: GamePlayerUiModel) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(player.name)
        if (player.isActive) {
            Text(ACTIVE_PLAYER_TEXT, style = MaterialTheme.typography.caption)
        }
    }
}

@Composable
private fun boardCard(
    uiState: GameUiState,
    onCellClick: (Int, Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(modifier = modifier) {
        Column(
            modifier = Modifier.padding(CARD_PADDING.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(CONTENT_SPACING.dp),
        ) {
            Text("Доска", style = MaterialTheme.typography.subtitle1)
            uiState.boardRows.forEach { row ->
                Row(horizontalArrangement = Arrangement.spacedBy(BOARD_GRID_SPACING.dp)) {
                    row.forEach { cell ->
                        boardCell(cell, onCellClick)
                    }
                }
            }
        }
    }
}

@Composable
private fun boardCell(
    cell: GameBoardCellUiModel,
    onCellClick: (Int, Int) -> Unit,
) {
    Box(
        modifier =
            Modifier
                .size(BOARD_CELL_SIZE.dp)
                .background(cell.backgroundColor())
                .border(BOARD_CELL_BORDER_WIDTH.dp, boardCellBorderColor)
                .clickable { onCellClick(cell.x, cell.y) },
        contentAlignment = Alignment.Center,
    ) {
        boardCellContent(cell)
        wallOverlay(cell)
    }
}

@Composable
private fun boardCellContent(cell: GameBoardCellUiModel) {
    Column(
        modifier = Modifier.padding(BOARD_CELL_PADDING.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        cell.topCardText?.let { topCardText ->
            Text(
                text = topCardText,
                fontSize = BOARD_TOP_CARD_FONT_SIZE.sp,
                textAlign = TextAlign.Center,
                maxLines = 1,
            )
        }
        Text(
            text = cell.shortText,
            fontSize = BOARD_CELL_FONT_SIZE.sp,
            textAlign = TextAlign.Center,
            maxLines = 1,
        )
    }
}

@Composable
private fun BoxScope.wallOverlay(cell: GameBoardCellUiModel) {
    if (cell.hasTopWall) {
        wallLine(Modifier.align(Alignment.TopCenter).fillMaxWidth().height(WALL_THICKNESS.dp))
    }
    if (cell.hasRightWall) {
        wallLine(Modifier.align(Alignment.CenterEnd).width(WALL_THICKNESS.dp).fillMaxHeight())
    }
}

@Composable
private fun wallLine(modifier: Modifier) {
    Box(modifier = modifier.background(wallColor))
}

@Composable
private fun selectedCellCard(cell: GameBoardCellUiModel?) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(CARD_PADDING.dp),
            verticalArrangement = Arrangement.spacedBy(CONTENT_SPACING.dp),
        ) {
            if (cell == null) {
                Text("Выберите клетку на доске", style = MaterialTheme.typography.body2)
            } else {
                selectedCellContent(cell)
            }
        }
    }
}

@Composable
private fun selectedCellContent(cell: GameBoardCellUiModel) {
    Text("Клетка ${cell.coordinatesText()}", style = MaterialTheme.typography.subtitle1)
    infoRow("Условия", cell.conditions.ifEmpty { listOf(EMPTY_LIST_TEXT) }.joinToString())
    infoRow("Стены", cell.walls.ifEmpty { listOf(EMPTY_LIST_TEXT) }.joinToString())
    if (cell.cards.isEmpty()) {
        Text("Карт нет", style = MaterialTheme.typography.body2)
    } else {
        cell.cards.forEach { card ->
            cardInfo(card)
        }
    }
}

@Composable
private fun cardInfo(card: GameCardUiModel) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text("• ${card.name}")
        Text(card.description, style = MaterialTheme.typography.caption)
        card.details.forEach { detail ->
            Text(detail, style = MaterialTheme.typography.caption)
        }
    }
}

@Composable
private fun inventoryCard(inventory: List<GameCardUiModel>) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(CARD_PADDING.dp),
            verticalArrangement = Arrangement.spacedBy(CONTENT_SPACING.dp),
        ) {
            Text("Инвентарь", style = MaterialTheme.typography.subtitle1)
            if (inventory.isEmpty()) {
                Text("Пусто", style = MaterialTheme.typography.body2)
            } else {
                inventory.forEach { item ->
                    cardInfo(item)
                }
            }
        }
    }
}

@Composable
private fun messageCard(uiState: GameUiState) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(CARD_PADDING.dp),
            verticalArrangement = Arrangement.spacedBy(CONTENT_SPACING.dp),
        ) {
            uiState.automaticActionName?.let { actionName ->
                Text("Последнее автоматическое действие: $actionName")
            }
            uiState.message?.let { message ->
                Text(message, style = MaterialTheme.typography.body2)
            }
        }
    }
}

@Composable
private fun actionsCard(
    actions: List<GameActionUiModel>,
    onActionClick: (Int) -> Unit,
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(CARD_PADDING.dp),
            verticalArrangement = Arrangement.spacedBy(CONTENT_SPACING.dp),
        ) {
            Text("Доступные действия", style = MaterialTheme.typography.subtitle1)
            if (actions.isEmpty()) {
                Text("Нет доступных ручных действий", style = MaterialTheme.typography.body2)
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxWidth().height(ACTIONS_LIST_HEIGHT.dp),
                    verticalArrangement = Arrangement.spacedBy(CONTENT_SPACING.dp),
                ) {
                    items(actions) { action ->
                        actionButton(
                            action = action,
                            onClick = { onActionClick(action.id) },
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun actionButton(
    action: GameActionUiModel,
    onClick: () -> Unit,
) {
    Button(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth().height(ACTION_BUTTON_HEIGHT.dp),
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(action.name)
            Text(action.description, style = MaterialTheme.typography.caption)
        }
    }
}

@Composable
private fun refreshButton(onClick: () -> Unit) {
    Spacer(modifier = Modifier.height(SMALL_SPACER_HEIGHT.dp))
    Button(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth().height(ACTION_BUTTON_HEIGHT.dp),
    ) {
        Text("Обновить")
    }
}

@Composable
private fun infoRow(
    label: String,
    value: String,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(label)
        Text(value)
    }
}

private fun GameBoardCellUiModel.backgroundColor(): Color {
    return when {
        isSelected -> selectedCellColor
        isActivePlayerCell -> activeCellColor
        cards.isNotEmpty() -> cardCellColor
        conditions.isNotEmpty() -> conditionCellColor
        else -> emptyCellColor
    }
}

private fun GameBoardCellUiModel.coordinatesText(): String = "($x, $y)"

private fun Pair<Int, Int>.formatCoordinates(): String = "($first, $second)"
