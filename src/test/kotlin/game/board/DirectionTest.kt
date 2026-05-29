package game.board

import kotlin.test.Test
import kotlin.test.assertEquals

class DirectionTest {
    @Test
    fun `direction exposes coordinate deltas and keys`() {
        assertEquals(0 to 1, Direction.UP.delta)
        assertEquals(0 to -1, Direction.DOWN.delta)
        assertEquals(1 to 0, Direction.RIGHT.delta)
        assertEquals(-1 to 0, Direction.LEFT.delta)
        assertEquals("enum.direction.up.name", Direction.UP.nameKey)
    }
}
