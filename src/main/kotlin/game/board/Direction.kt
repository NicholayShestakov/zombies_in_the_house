package game

enum class Direction(val delta: Pair<Int, Int>, val nameKey: String) {
    UP(Pair(1, 0), "enum.direction.up.name"),
    DOWN(Pair(-1, 0), "enum.direction.down.name"),
    RIGHT(Pair(0, 1), "enum.direction.right.name"),
    LEFT(Pair(0, -1), "enum.direction.left.name");
}