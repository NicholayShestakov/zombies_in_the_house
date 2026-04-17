enum class Direction(val delta: Pair<Int, Int>) {
    UP(Pair(1, 0)),
    DOWN(Pair(-1, 0)),
    RIGHT(Pair(0, 1)),
    LEFT(Pair(0, -1));
}