class DayFifteen(file: String) : Project {
    private val grid = mapLettersPerLines(file) { it.map { c -> Character.getNumericValue(c) } }
    private val nodes = HashMap<Pair<Int, Int>, Density>()
    private val unvisited = HashMap<Pair<Int, Int>, Density>()
    private var lowest: Density? = null

    init {
        for (row in grid.indices) {
            for (col in grid[row].indices) {
                val coord = Pair(col, row)
                val isStart = col == 0 && row == 0

                nodes[coord] = Density(coord, neighbours(coord), grid[row][col])
                unvisited[coord] = nodes[coord]!!

                if (isStart) {
                    lowest = nodes[coord]
                    lowest?.shortestRisk = grid[row][col]
                }
            }
        }

        while (unvisited.isNotEmpty()) {
            val curr = lowest!!

            neighbours(curr.coord)
                .map { nodes[Pair(it.first, it.second)] }
                .filter { it != null && unvisited.containsKey(it.coord)}
                .forEach {
                    if (it != null && curr.shortestRisk + it.risk < it.shortestRisk) {
                        it.shortestRisk = curr.shortestRisk + it.risk
                    }
                }

            unvisited.remove(curr.coord)
            lowest = unvisited.values.minByOrNull { it.shortestRisk }
        }

    }

    override fun part1(): Any {
        return nodes[Pair(grid.size - 1, grid[grid.size - 1].size - 1)]?.shortestRisk!! - nodes[Pair(0, 0)]?.shortestRisk!!
    }

    override fun part2(): Any {
        return -1
    }

    private fun neighbours(coords: Pair<Int, Int>): List<Pair<Int, Int>> {
        return listOf(
            Pair(coords.first - 1, coords.second),
            Pair(coords.first + 1, coords.second),
            Pair(coords.first, coords.second - 1),
            Pair(coords.first, coords.second + 1)
        ).filter { it.first >= 0 && it.first < grid.size && it.second >= 0 && it.second < grid[it.first].size }
    }

    private fun printGrid(curr: Pair<Int, Int>) {
        for (row in grid.indices) {
            for (col in grid[row].indices) {
                val coord = Pair(row, col)
                val density: Density = nodes[coord]!!
                print(if (curr == coord) "C" else if (density.shortestRisk > 9) "*" else density.shortestRisk)
            }
            println()
        }
        println()

    }
}

class Density(val coord: Pair<Int, Int>, val links: List<Pair<Int, Int>>, var risk: Int) {
    var shortestRisk = Int.MAX_VALUE
}