package game.gameControl.contextManagers

import game.Player
import game.cards.Entity

class PlayersManager(val players: List<Player>) {
    fun getPlayerWithEntity(entity: Entity): Player? = players.firstOrNull { it.currentEntity == entity }

    fun getPlayerWithEntityIndex(entity: Entity): Int = players.indexOfFirst { it.currentEntity == entity }
}
