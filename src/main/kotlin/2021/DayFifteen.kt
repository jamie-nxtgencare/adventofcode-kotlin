@file:Suppress("PackageName")
package `2021`
import Project
import kotlin.math.floor

class DayFifteen(file: String, isTest: Boolean = false) : Project(file, isTest) {
    private val grid = mapLettersPerLines(file) { it.map { c -> Character.getNumericValue(c) } }
    private val nodes = HashMap<Pair<Int, Int>, Density>()
    private val unvisited = HashMap<Pair<Int, Int>, Density>()
    private var lowest: Density? = null
    private var first: Density? = null
    private var last: Density? = null

    private val nodes2 = HashMap<Pair<Int, Int>, Density>()
    private val unvisited2 = HashMap<Pair<Int, Int>, Density>()
    private var lowest2: Density? = null
    private var first2: Density? = null
    private var last2: Density? = null

    init {

        val lastCoords = Pair(grid[0].size, grid.size)
        val lastCoords2 = Pair(grid[0].size * 5, grid.size * 5)
        for (row in grid.indices) {
            for (col in grid[row].indices) {
                val coord = Pair(col, row)
                val isStart = col == 0 && row == 0

                nodes[coord] = Density(coord, neighbours(coord, lastCoords), grid[row][col])
                unvisited[coord] = nodes[coord]!!

                if (isStart) {
                    lowest = nodes[coord]
                    lowest?.shortestRisk = grid[row][col]
                    first = lowest
                }
                last = nodes[coord]

                for (i in 0 until 5) {
                    for (j in 0 until 5) {
                        val isStart2 = isStart && i == 0 && j == 0
                        val coord2 = Pair(col + (i * grid.size), row + (j * grid.size))

                        var risk = grid[row][col] + (i + j)
                        val fudge = floor(risk / 10.0).toInt()
                        risk = (risk + fudge) % 10

                        nodes2[coord2] = Density(coord2, neighbours(coord2, lastCoords2), risk)
                        unvisited2[coord2] = nodes2[coord2]!!

                        if (isStart2) {
                            lowest2 = nodes2[coord2]
                            lowest2?.shortestRisk = lowest2?.risk!!
                            first2 = lowest2
                        }
                        last2 = nodes2[coord2]
                    }
                }
            }
        }

        dijkstra(unvisited, nodes, lowest)
        dijkstra(unvisited2, nodes2, lowest2)
    }

    private fun dijkstra(unvisited: java.util.HashMap<Pair<Int, Int>, Density>, nodes: java.util.HashMap<Pair<Int, Int>, Density>, start: Density?) {
        var lowest = start
        val assessedUnvisited = HashSet<Density>()

        while (unvisited.isNotEmpty()) {
            val curr = lowest!!

            curr.links
                .map { nodes[Pair(it.first, it.second)] }
                .filter { it != null && unvisited.containsKey(it.coord)}
                .forEach {
                    if (it != null && curr.shortestRisk + it.risk < it.shortestRisk) {
                        it.shortestRisk = curr.shortestRisk + it.risk
                        assessedUnvisited.add(it)
                    }
                }

            unvisited.remove(curr.coord)
            lowest = assessedUnvisited.minByOrNull { it.shortestRisk }
            assessedUnvisited.remove(lowest)
        }
    }

    override suspend fun part1(): Any {
        return last?.shortestRisk!! - first?.risk!!
    }

    override suspend fun part2(): Any {
        /*println(printGrid())
        println(path(last2))*/
        return last2?.shortestRisk!! - first2?.risk!!
    }

    private fun path(last2: Density?) {
        val l = ArrayList<Pair<Int, Int>>()

        var c = last2
        while (c != first2) {
            l.add(c?.coord!!)
            print(c.risk)
            c = nodes2[c.links.filter { !l.contains(it) }.minByOrNull { nodes2[it]?.shortestRisk ?: Int.MAX_VALUE }]
        }
    }

    private fun printGrid() {
        for (row in 0..49) {
            for (col in 0..49) {
                val coord = Pair(col, row)
                val density: Density = nodes2[coord]!!
                print(density.risk)
            }
            println()
        }
        println()

    }

    private fun neighbours(coords: Pair<Int, Int>, last: Pair<Int, Int>): List<Pair<Int, Int>> {
        return listOf(
            Pair(coords.first - 1, coords.second),
            Pair(coords.first + 1, coords.second),
            Pair(coords.first, coords.second - 1),
            Pair(coords.first, coords.second + 1)
        ).filter { it.first >= 0 && it.first <= last.first && it.second >= 0 && it.second <= last.second }
    }
}

class Density(val coord: Pair<Int, Int>, val links: List<Pair<Int, Int>>, var risk: Int) {
    var shortestRisk = Int.MAX_VALUE
}