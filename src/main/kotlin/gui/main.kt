@file:Suppress("ktlint:standard:filename")

package gui

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import game.actions.ActionManager
import game.gameControl.ReadOnlyGameContext
import kotlinx.coroutines.flow.collectLatest

private const val WINDOW_TITLE = "Zombies in the house"
private const val INIT_SCREEN_ROUTE = "init_screen"
private const val GAME_SCREEN_ROUTE = "game_screen"
private const val FINAL_SCREEN_ROUTE = "final_screen"

fun main() =
    application {
        Window(
            onCloseRequest = ::exitApplication,
            title = WINDOW_TITLE,
        ) {
            appNavigation(onExitClick = ::exitApplication)
        }
    }

@Composable
@Preview
fun appNavigation(onExitClick: () -> Unit = {}) {
    val navController = rememberNavController()

    val statisticsRepository = remember { PlayerStatisticsRepository() }

    var savedActionManager by remember { mutableStateOf<ActionManager?>(null) }
    var savedReadOnlyContext by remember { mutableStateOf<ReadOnlyGameContext?>(null) }
    var savedFinalGame by remember { mutableStateOf<FinalGameUiModel?>(null) }

    NavHost(
        navController = navController,
        startDestination = INIT_SCREEN_ROUTE,
    ) {
        composable(INIT_SCREEN_ROUTE) {
            val initViewModel: InitViewModel = viewModel { InitViewModel() }

            LaunchedEffect(initViewModel) {
                initViewModel.navigationEvents.collectLatest { managerAndContext ->
                    savedActionManager = managerAndContext.first
                    savedReadOnlyContext = managerAndContext.second

                    navController.navigate(GAME_SCREEN_ROUTE) {
                        popUpTo(INIT_SCREEN_ROUTE) { inclusive = true }
                    }
                }
            }

            initScreen(viewModel = initViewModel)
        }

        composable(GAME_SCREEN_ROUTE) {
            val manager = savedActionManager ?: error("Action manager is not initialized")
            val context = savedReadOnlyContext ?: error("Read only context is not initialized")

            val gameViewModel: GameViewModel = viewModel { GameViewModel(manager, context) }

            gameScreen(
                gameViewModel = gameViewModel,
                onGameFinished = { finalGame ->
                    statisticsRepository.recordFinalGame(finalGame)
                    savedFinalGame = finalGame
                    navController.navigate(FINAL_SCREEN_ROUTE) {
                        popUpTo(GAME_SCREEN_ROUTE) { inclusive = true }
                    }
                },
            )
        }

        composable(FINAL_SCREEN_ROUTE) {
            val finalGame = savedFinalGame ?: error("Final game state is not initialized")

            finalScreen(
                finalGame = finalGame,
                onExitClick = onExitClick,
            )
        }
    }
}
