package game.gameControl.contextManagers

import game.Player

class TurnManager(val players: List<Player>) {
    var activePlayerIndex = 0
    val activePlayer: Player get() = players[activePlayerIndex]

    fun nextTurn() {
        activePlayerIndex = (activePlayerIndex + 1) % players.size
    }
}
