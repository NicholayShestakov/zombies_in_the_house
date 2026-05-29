@file:Suppress("ktlint:standard:filename")

package gui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Button
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Slider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

private const val INIT_SETTINGS_WIDTH = 400
private const val STATISTICS_PANEL_WIDTH = 280
private const val INIT_SCREEN_SPACING = 32
private const val PLAYER_COUNT_SLIDER_WIDTH = 200
private const val START_BUTTON_HEIGHT = 50
private const val STATISTICS_LIST_HEIGHT = 220
private const val SECTION_SPACING = 16
private const val SMALL_SECTION_SPACING = 8
private const val STATISTICS_ROW_SPACING = 4

@Composable
fun initScreen(viewModel: InitViewModel) {
    val uiState by viewModel.uiState.collectAsState()

    Box(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        contentAlignment = Alignment.Center,
    ) {
        initContent(uiState, viewModel)
    }
}

@Composable
private fun initContent(
    uiState: InitUiState,
    viewModel: InitViewModel,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(INIT_SCREEN_SPACING.dp),
        verticalAlignment = Alignment.Top,
    ) {
        settingsForm(uiState, viewModel)
        statisticsSection(uiState.statistics)
    }
}

@Composable
private fun settingsForm(
    uiState: InitUiState,
    viewModel: InitViewModel,
) {
    LazyColumn(
        modifier = Modifier.width(INIT_SETTINGS_WIDTH.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(SECTION_SPACING.dp),
    ) {
        item {
            Text("Настройки новой игры", style = MaterialTheme.typography.h5)
        }

        item {
            playerCountSelector(uiState.playerCount, viewModel::changePlayerCount)
        }

        item {
            startHealthInput(uiState.startHealth, viewModel::changeStartHealth)
        }

        item {
            playerNamesHeader()
        }

        itemsIndexed(uiState.playerNames) { index, name ->
            playerNameInput(index, name, viewModel::changePlayerName)
        }

        uiState.errorMessage?.let { errorMessage ->
            item {
                errorText(errorMessage)
            }
        }

        item {
            startGameButton(viewModel::onStartGameClicked)
        }
    }
}

@Composable
private fun statisticsSection(statistics: List<PlayerStatisticsUiModel>) {
    Column(
        modifier = Modifier.width(STATISTICS_PANEL_WIDTH.dp),
        verticalArrangement = Arrangement.spacedBy(SMALL_SECTION_SPACING.dp),
    ) {
        Text("Статистика побед", style = MaterialTheme.typography.h6)
        Divider()
        statisticsHeader()
        if (statistics.isEmpty()) {
            Text("Пока нет побед", style = MaterialTheme.typography.body2)
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxWidth().height(STATISTICS_LIST_HEIGHT.dp),
                verticalArrangement = Arrangement.spacedBy(STATISTICS_ROW_SPACING.dp),
            ) {
                items(statistics.size) { index ->
                    statisticsRow(statistics[index])
                }
            }
        }
    }
}

@Composable
private fun statisticsHeader() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text("Имя")
        Text("Победы")
    }
}

@Composable
private fun statisticsRow(statistics: PlayerStatisticsUiModel) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(statistics.name)
        Text(statistics.wins.toString())
    }
}

@Composable
private fun playerCountSelector(
    playerCount: Int,
    onCountChanged: (Int) -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text("Количество игроков: $playerCount")
        Slider(
            value = playerCount.toFloat(),
            onValueChange = { onCountChanged(it.toInt()) },
            valueRange = 1f..5f,
            steps = 3,
            modifier = Modifier.width(PLAYER_COUNT_SLIDER_WIDTH.dp),
        )
    }
}

@Composable
private fun startHealthInput(
    startHealth: Int,
    onStartHealthChanged: (Int) -> Unit,
) {
    OutlinedTextField(
        value = startHealth.toString(),
        onValueChange = { text ->
            val health = text.toIntOrNull() ?: 1
            onStartHealthChanged(health)
        },
        label = { Text("Стартовое здоровье") },
        modifier = Modifier.fillMaxWidth(),
    )
}

@Composable
private fun playerNamesHeader() {
    Divider()
    Spacer(modifier = Modifier.height(SMALL_SECTION_SPACING.dp))
    Text("Имена игроков", style = MaterialTheme.typography.subtitle1)
}

@Composable
private fun playerNameInput(
    index: Int,
    name: String,
    onNameChanged: (Int, String) -> Unit,
) {
    OutlinedTextField(
        value = name,
        onValueChange = { newName -> onNameChanged(index, newName) },
        label = { Text("Игрок ${index + 1}") },
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
    )
}

@Composable
private fun errorText(errorMessage: String) {
    Text(
        text = errorMessage,
        color = MaterialTheme.colors.error,
        style = MaterialTheme.typography.body2,
    )
}

@Composable
private fun startGameButton(onClick: () -> Unit) {
    Spacer(modifier = Modifier.height(SECTION_SPACING.dp))
    Button(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth().height(START_BUTTON_HEIGHT.dp),
    ) {
        Text("Создать игру")
    }
}
