package game.board

enum class Direction(val delta: Pair<Int, Int>, val nameKey: String) {
    UP(Pair(0, 1), "enum.direction.up.name"),
    DOWN(Pair(0, -1), "enum.direction.down.name"),
    RIGHT(Pair(1, 0), "enum.direction.right.name"),
    LEFT(Pair(-1, 0), "enum.direction.left.name"),
}
