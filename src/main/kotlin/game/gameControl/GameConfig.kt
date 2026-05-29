package game.gameControl

data class GameConfig(
    val playerCount: Int,
    val playerNames: List<String>,
    val startHealth: Int,
    val localization: String = "ru",
)
