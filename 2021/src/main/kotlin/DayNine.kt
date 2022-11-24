class DayNine(file: String) : Project {
    private val grid: List<List<Int>> = mapLettersPerLines(file) { it.map { c -> Character.getNumericValue(c) }}
    private val lowPoints = ArrayList<Pair<Int, Int>>()

    override fun part1(): Any {
        var out = 0
        for (i in grid.indices) {
            val row = grid[i]
            for (j in row.indices) {
                val curr = row[j]
                val low = listOf(safeGet(grid, i - 1, j), safeGet(grid, i + 1, j), safeGet(grid, i, j - 1), safeGet(grid, i, j + 1)).all { curr < it }

                if (low) {
                    lowPoints.add(Pair(i, j))
                    out += curr + 1
                }
            }
        }

        return out
    }

    override fun part2(): Any {
        val basins = lowPoints.map { Basin(grid, it) }
        val threeLargest = basins.sortedBy { it.size()*-1 }.subList(0, 3)
        return threeLargest.fold(1) { acc, next -> acc * next.size() }
    }

    private fun safeGet(grid: List<List<Int>>, i: Int, j: Int): Int {
        return if (i >= 0 && i < grid.size && j >= 0 && j < grid[i].size) grid[i][j] else Int.MAX_VALUE
    }
}

class Basin(private val grid: List<List<Int>>, low: Pair<Int, Int>) {
    private var points = ArrayList<Pair<Int, Int>>()

    init {
        points.add(low)
        val higherNeighbours = neighbours(low).filter { grid[it.first][it.second] < 9 && grid[it.first][it.second] > grid[low.first][low.second] }
        val subBasins = higherNeighbours.map { Basin(grid, it) }
        points.addAll(subBasins.map { it.points }.flatten())
        points = ArrayList(points.distinct())
    }

    fun size(): Int {
        return points.size
    }

    private fun neighbours(coords: Pair<Int, Int>): List<Pair<Int, Int>> {
        return listOf(
            Pair(coords.first - 1, coords.second),
            Pair(coords.first + 1, coords.second),
            Pair(coords.first, coords.second - 1),
            Pair(coords.first, coords.second + 1)
        ).filter { it.first >= 0 && it.first < grid.size && it.second >= 0 && it.second < grid[it.first].size }
    }
}
