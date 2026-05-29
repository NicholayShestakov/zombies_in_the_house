package gui

data class InitUiState(
    val playerCount: Int = 1,
    val playerNames: List<String> = listOf("Player 1"),
    val startHealth: Int = 3,
    val statistics: List<PlayerStatisticsUiModel> = emptyList(),
    val errorMessage: String? = null,
)

data class PlayerStatisticsUiModel(
    val name: String,
    val wins: Int,
)
