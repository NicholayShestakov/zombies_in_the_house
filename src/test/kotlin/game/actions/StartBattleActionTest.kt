package game.actions

import game.Player
import game.SPINNER_AIM_SEED
import game.SPINNER_ESCAPE_SEED
import game.START
import game.board.Board
import game.cards.Entity
import game.gameControl.GameContext
import game.testBoard
import game.testHuman
import game.testMonster
import game.testPlayer
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertTrue

class StartBattleActionTest {
    private lateinit var entities: List<Entity>
    private lateinit var players: List<Player>
    private lateinit var board: Board
    private lateinit var context: GameContext
    private lateinit var action: StartBattleAction

    @BeforeEach
    fun initialization() {
        entities =
            listOf(
                testHuman(plusSpeed = -1),
                testMonster(),
            )
        players =
            listOf(
                testPlayer("Alice", entities[0]),
                testPlayer("Bob", entities[1]),
            )
        board = testBoard(cards = mapOf(START to entities))
        context = GameContext(board, players, "ru")
        action = StartBattleAction(context)
    }

    @Test
    fun `start battle action available when human on one cell with monster`() {
        context.spin(SPINNER_AIM_SEED)
        context.activePlayerStartStepping()
        assertTrue(action.isAvailable)
    }

    @Test
    fun `start battle action available when monster on one cell with human`() {
        context.spin(SPINNER_ESCAPE_SEED)
        context.activePlayerStartStepping()
        context.nextTurn()

        context.activePlayerSetEntity(entities[1])
        context.spin()
        context.activePlayerStartStepping()
        assertTrue(action.isAvailable)
    }
}
