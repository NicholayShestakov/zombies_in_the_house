package game.gameControl

class GameInitialization {
    var playerCount = 1
        private set
    private val playerNames = mutableListOf("Player 1", "Player 2", "Player 3", "Player 4", "Player 5")
    val playerNamesList: List<String>
        get() = playerNames.subList(0, playerCount).toList()
    var startHealth = 3
        private set

    fun setPlayerCount(count: Int): Int {
        require(count in 1..5) { "Count must be between 1 and 5" }
        playerCount = count
        return count
    }

    fun setPlayerName(
        index: Int,
        name: String,
    ): String {
        require(index in 0..<playerCount) { "Name index must be between 0 and 4" }
        require(name !in playerNames.filterIndexed { i, _ -> i != index }) { "Names cannot be repeated" }
        playerNames[index] = name
        return name
    }

    fun setPlayerStartHealth(health: Int): Int {
        require(health >= 1) { "Health must be greater than 0" }
        startHealth = health
        return health
    }

    fun buildGameConfig(): GameConfig {
        require(playerNames.subList(0, playerCount).all { it.isNotBlank() }) { "Names cannot be empty" }
        return GameConfig(
            playerCount,
            playerNames.subList(0, playerCount).toList(),
            startHealth,
        )
    }
}
