package gui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import game.actions.ActionManager
import game.gameControl.GameInitialization
import game.gameControl.GameStarter
import game.gameControl.ReadOnlyGameContext
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class InitViewModel(
    private val statisticsRepository: PlayerStatisticsRepository = PlayerStatisticsRepository(),
) : ViewModel() {
    private val gameInit = GameInitialization()

    private val _uiState = MutableStateFlow(InitUiState())
    val uiState = _uiState.asStateFlow()

    private val _navigationEvents = MutableSharedFlow<Pair<ActionManager, ReadOnlyGameContext>>()
    val navigationEvents = _navigationEvents.asSharedFlow()

    init {
        updateUiState()
    }

    fun changePlayerCount(count: Int) {
        try {
            gameInit.setPlayerCount(count)
            updateUiState()
        } catch (e: IllegalArgumentException) {
            _uiState.update {
                it.copy(errorMessage = e.message)
            }
        }
    }

    fun changePlayerName(
        index: Int,
        name: String,
    ) {
        try {
            gameInit.setPlayerName(index, name)
            updateUiState()
        } catch (e: IllegalArgumentException) {
            _uiState.update {
                it.copy(errorMessage = e.message)
            }
        }
    }

    fun changeStartHealth(health: Int) {
        try {
            gameInit.setPlayerStartHealth(health)
            updateUiState()
        } catch (e: IllegalArgumentException) {
            _uiState.update {
                it.copy(errorMessage = e.message)
            }
        }
    }

    fun onStartGameClicked() {
        try {
            val config = gameInit.buildGameConfig()
            val starter = GameStarter()
            val (manager, context) = starter.start(config)

            viewModelScope.launch {
                _navigationEvents.emit(manager to context)
            }
        } catch (e: IllegalArgumentException) {
            _uiState.update {
                it.copy(errorMessage = e.message)
            }
        }
    }

    private fun updateUiState() {
        _uiState.update {
            InitUiState(
                playerCount = gameInit.playerCount,
                playerNames = gameInit.playerNamesList,
                startHealth = gameInit.startHealth,
                statistics = statisticsRepository.loadStatistics(),
                errorMessage = null,
            )
        }
    }
}
