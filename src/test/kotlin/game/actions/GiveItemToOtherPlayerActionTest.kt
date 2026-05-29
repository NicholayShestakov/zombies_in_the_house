package game.actions

import game.Player
import game.SPINNER_ESCAPE_SEED
import game.START
import game.board.Board
import game.cards.Entity
import game.gameControl.GameContext
import game.testBoard
import game.testHuman
import game.testItem
import game.testMonster
import game.testPlayer
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class GiveItemToOtherPlayerActionTest {
    private lateinit var entities: List<Entity>
    private lateinit var players: List<Player>
    private lateinit var board: Board
    private lateinit var context: GameContext

    @BeforeEach
    fun initialization() {
        entities =
            listOf(
                testHuman(plusSpeed = -1),
                testHuman(plusSpeed = -1),
                testMonster(plusSpeed = -1),
            )
        players =
            listOf(
                testPlayer("Alice", entities[0]),
                testPlayer("Bob", entity = entities[1]),
                testPlayer("Cartoshka", entity = entities[2]),
            )
        board = testBoard(cards = mapOf(START to entities))
        context = GameContext(board, players, "ru")
    }

    @Test
    fun `player can get item only to other human player`() {
        context.spin(SPINNER_ESCAPE_SEED)
        context.activePlayerStartStepping()

        val item = testItem()
        context.playerWithEntityAddItem(entities[0], item)
        val action = entities.map { GiveItemToOtherPlayerAction(context, it, item.itemType) }

        assertFalse(action[0].isAvailable)
        assertTrue(action[1].isAvailable)
        assertFalse(action[2].isAvailable)
    }
}
