package game.factories

import game.Player
import game.cards.Entity
import game.cards.EntityAbility
import game.cards.Item
import game.cards.ItemType

class PlayerFactory {
    fun createPlayers(
        names: List<String>,
        humans: List<Entity>,
    ): List<Player> {
        val players =
            names.map {
                Player(it, humans[names.indexOf(it)])
            }

        players.forEach {
            if (it.currentEntity.abilities.contains(EntityAbility.START_KNIFE)) it.inventory.add(Item(ItemType.KNIFE))
            if (it.currentEntity.abilities.contains(EntityAbility.START_GUN)) it.inventory.add(Item(ItemType.GUN))
        }

        return players
    }
}
