@file:Suppress("ktlint:standard:filename")

package gui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

private const val FINAL_SCREEN_WIDTH = 420
private const val FINAL_CARD_PADDING = 24
private const val FINAL_CONTENT_SPACING = 16

@Composable
fun finalScreen(
    finalGame: FinalGameUiModel,
    onExitClick: () -> Unit,
) {
    Box(
        modifier = Modifier.fillMaxSize().padding(FINAL_CARD_PADDING.dp),
        contentAlignment = Alignment.Center,
    ) {
        Card(modifier = Modifier.width(FINAL_SCREEN_WIDTH.dp)) {
            Column(
                modifier = Modifier.padding(FINAL_CARD_PADDING.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(FINAL_CONTENT_SPACING.dp),
            ) {
                finalGameContent(finalGame)
                Button(
                    onClick = onExitClick,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text("Выйти из игры")
                }
            }
        }
    }
}

@Composable
private fun finalGameContent(finalGame: FinalGameUiModel) {
    if (finalGame.isWin) {
        Text("Победа!", style = MaterialTheme.typography.h5)
        Text("Победители:", style = MaterialTheme.typography.subtitle1)
        if (finalGame.winnerNames.isEmpty()) {
            Text("Игроки на финише не найдены", style = MaterialTheme.typography.body2)
        } else {
            finalGame.winnerNames.forEach { winnerName ->
                Text("• $winnerName")
            }
        }
    } else {
        Text("Поражение", style = MaterialTheme.typography.h5)
        Text("Все игроки проиграли.", style = MaterialTheme.typography.body2)
    }
}
