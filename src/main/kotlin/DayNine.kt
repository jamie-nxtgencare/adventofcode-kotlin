class DayNine(file: String) : Project {
    val grid: List<List<Int>> = mapLettersPerLines(file) { it.map { c -> Character.getNumericValue(c) }}

    override fun part1(): Any {
        var out = 0
        for (i in grid.indices) {
            val row = grid[i]
            for (j in row.indices) {
                val curr = row[j]
                val low = listOf(safeGet(grid, i - 1, j), safeGet(grid, i + 1, j), safeGet(grid, i, j - 1), safeGet(grid, i, j + 1)).all { curr < it }

                if (low) {
                    out += curr + 1
                }
            }
        }

        return out
    }

    private fun safeGet(grid: List<List<Int>>, i: Int, j: Int): Int {
        return if (i >= 0 && i < grid.size && j >= 0 && j < grid[i].size) grid[i][j] else Int.MAX_VALUE
    }

    override fun part2(): Any {
        return -1
    }

}