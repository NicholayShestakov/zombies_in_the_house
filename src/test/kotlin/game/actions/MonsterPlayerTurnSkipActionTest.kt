package game.actions

import game.Player
import game.SPINNER_ESCAPE_SEED
import game.START
import game.board.Board
import game.cards.Entity
import game.gameControl.GameContext
import game.gameControl.GameState
import game.testBoard
import game.testHuman
import game.testMonster
import game.testPlayer
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class MonsterPlayerTurnSkipActionTest {
    private lateinit var entities: List<Entity>
    private lateinit var players: List<Player>
    private lateinit var board: Board
    private lateinit var context: GameContext
    private lateinit var action: MonsterPlayerTurnSkipAction

    @BeforeEach
    fun initialization() {
        entities =
            listOf(
                testHuman(plusSpeed = -1),
                testHuman(healthPoints = 0),
                testMonster(),
            )
        players =
            listOf(
                testPlayer("Alice", entities[0]),
                testPlayer("Bob", entity = entities[1]),
                testPlayer("Cartoshka", entity = entities[2]),
            )
        board = testBoard(cards = mapOf(START to entities))
        context = GameContext(board, players, "ru")
        action = MonsterPlayerTurnSkipAction(context)
    }

    @Test
    fun `action skips player turn if available monsters does not exist`() {
        context.spin(SPINNER_ESCAPE_SEED)
        context.activePlayerStartStepping()
        context.nextTurn()

        assertEquals(GameState.MONSTER_CHOOSING, context.gameState)
        assertEquals(1, context.activePlayerIndex)
        assertTrue(action.isAvailable)
        action.execute()
        assertEquals(GameState.MONSTER_CHOOSING, context.gameState)
        assertEquals(2, context.activePlayerIndex)
        assertFalse(action.isAvailable)
    }
}
