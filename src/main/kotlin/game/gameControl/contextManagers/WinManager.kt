package game.gameControl.contextManagers

import game.Player

class WinManager(val winConditionsToWin: Int) {
    var winConditions = 0
        private set

    fun incrementWinConditions() {
        winConditions++
    }

    fun isWin(): Boolean {
        return winConditions >= winConditionsToWin
    }

    fun isLose(players: List<Player>): Boolean {
        return players.none { it.playsAsHuman }
    }
}
