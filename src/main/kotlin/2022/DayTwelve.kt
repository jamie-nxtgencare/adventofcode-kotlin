@file:Suppress("PackageName")

package `2022`

import Project
import java.lang.Integer.min
import kotlin.streams.toList

class DayTwelve(file: String) : Project {
    var col = 0
    val grid = mapFileLines(file) {
        val rowData = it.chars().toList().mapIndexed { index, elem -> Node(elem, index, col) }
        col++
        rowData
    }

    val start = grid.flatten().first { it.heightOrStartOrEnd == 'S'.code }
    val end = grid.flatten().first { it.heightOrStartOrEnd == 'E'.code }

    class Node(val heightOrStartOrEnd: Int, val x: Int, val y: Int) {
        val height = if (heightOrStartOrEnd == 'S'.code) 'a'.code else if (heightOrStartOrEnd == 'E'.code) 'z'.code else heightOrStartOrEnd
        var steps = Int.MAX_VALUE
    }

    override fun part1(): Any {
        start.steps = 0
        val toVisit: MutableList<Node> = listOf(start).toMutableList()
        val visited = mutableSetOf<Node>()

        while(toVisit.size > 0) {
            val visiting = toVisit.removeFirst()
            val validNeighbours = getNeighbours(visiting).filter { it.height <= visiting.height + 1 }
            validNeighbours.forEach { it.steps = min(it.steps, visiting.steps + 1) }
            toVisit.addAll(validNeighbours.filter { !toVisit.contains(it) && !visited.contains(it) })
            visited.add(visiting)
        }

        return end.steps
    }

    private fun getNeighbours(visiting: Node): List<Node> {
        return listOfNotNull(
            safeGet(visiting.y - 1, visiting.x), // Below
            safeGet(visiting.y + 1, visiting.x), // Above
            safeGet(visiting.y, visiting.x - 1), // Left
            safeGet(visiting.y, visiting.x + 1) // Right
        )
    }

    private fun safeGet(y: Int, x: Int): Node? {
        if (x < 0 || y < 0 || y >= grid.size || x >= grid[0].size) {
            return null
        }
        return grid[y][x]
    }

    override fun part2(): Any {
        return -1
    }

}