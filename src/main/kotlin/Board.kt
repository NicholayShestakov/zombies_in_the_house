class Board(
    private val width: Int,
    private val height: Int,
    private val cellList: List<List<Cell>>,
    private val wallList: List<Pair<Pair<Int, Int>, Pair<Int, Int>>>
) {

    fun getCells(): List<Cell> {
        return cellList.flatten().toSet().toList()
    }

    fun getCells(cellType: CellCondition): List<Cell> {
        return cellList.flatten().filter { cellType in it.conditions }.toSet().toList()
    }

    fun getCell(coordinates: Pair<Int, Int>): Cell? {
        if (coordinates.first !in 0..<width ||
            coordinates.second !in 0..<height) {
            return null
        }
        return cellList[coordinates.first][coordinates.second]
    }

    fun getNeighbour(cell: Cell, direction: Direction): Cell? {
        val neighbourCoordinates = Pair(
            cell.coordinates.first + direction.delta.first,
            cell.coordinates.second + direction.delta.second
        )

        if (Pair(neighbourCoordinates, cell.coordinates) in wallList ||
            Pair(cell.coordinates, neighbourCoordinates) in wallList) {
            return null
        }
        return getCell(neighbourCoordinates)
    }
}