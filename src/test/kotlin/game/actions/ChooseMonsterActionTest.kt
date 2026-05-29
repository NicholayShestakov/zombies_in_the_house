package game.actions

import game.FINISH
import game.Player
import game.SPINNER_ESCAPE_SEED
import game.START
import game.UP
import game.board.Board
import game.cards.Entity
import game.gameControl.GameContext
import game.testBoard
import game.testHuman
import game.testMonster
import game.testPlayer
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ChooseMonsterActionTest {
    private lateinit var humans: List<Entity>
    private lateinit var monsters: List<Entity>
    private lateinit var nonplayerMonsters: List<Entity>
    private lateinit var players: List<Player>
    private lateinit var board: Board
    private lateinit var context: GameContext
    private lateinit var action: List<ChooseMonsterAction>

    @BeforeEach
    fun initialization() {
        humans =
            listOf(
                testHuman("Human", plusSpeed = -4),
            )
        monsters =
            listOf(
                testMonster("PlayerMonster"),
                testMonster("PlayerMonster2"),
            )
        nonplayerMonsters =
            listOf(
                testMonster("NonplayerMonster"),
            )
        players =
            listOf(
                testPlayer("Alice", humans[0]),
                testPlayer("Bob", monsters[0]),
                testPlayer("Cartoshka", monsters[1]),
            )
        board = testBoard(cards = mapOf(START to humans, FINISH to monsters, UP to nonplayerMonsters))
        context = GameContext(board, players, "ru")
        action = (monsters + nonplayerMonsters).map { ChooseMonsterAction(context, it) }
    }

    @Test
    fun `human player cannot get availability of monster choosing action`() {
        action.forEach { assertFalse(it.isAvailable) }
    }

    @Test
    fun `monster player can choose nonplayer monster`() {
        context.spin(SPINNER_ESCAPE_SEED)
        context.activePlayerStartStepping()
        context.nextTurn()
        assertTrue(action[2].isAvailable)
    }

    @Test
    fun `monster player can choose his monster`() {
        context.spin(SPINNER_ESCAPE_SEED)
        context.activePlayerStartStepping()
        context.nextTurn()
        assertTrue(action[0].isAvailable)
    }

    @Test
    fun `monster player cannot choose other player monster`() {
        context.spin(SPINNER_ESCAPE_SEED)
        context.activePlayerStartStepping()
        context.nextTurn()
        assertFalse(action[1].isAvailable)
    }
}
