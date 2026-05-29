package game.gameControl

import game.actions.ActionManager
import game.factories.BoardFactory
import game.factories.DeckFactory
import game.factories.HumanFactory
import game.factories.PlayerFactory

class GameStarter {
    fun start(
        config: GameConfig,
        seed: Int? = null,
    ): Pair<ActionManager, ReadOnlyGameContext> {
        val humans = HumanFactory().createDefaultShuffledHumans(config.playerCount, config.startHealth, seed)
        val players = PlayerFactory().createPlayers(config.playerNames, humans)

        val deck = DeckFactory().createDefaultCardDeck()
        val board = BoardFactory().createDefaultBoard(humans, deck, seed)

        val context = GameContext(board, players, config.localization, seed = seed)
        val readOnlyContext = ReadOnlyGameContext(context)
        val manager = ActionManager(context)

        return manager to readOnlyContext
    }
}
