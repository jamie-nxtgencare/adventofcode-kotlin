@file:Suppress("PackageName")

package `2023`

import Project
import java.lang.Character.isDigit
import java.lang.Integer.parseInt
import kotlin.math.max
import kotlin.math.min

class DayThree(file: String) : Project() {
    private val lines = getLines(file)
    private val grid: List<List<Char>> = lines.map { it.split("").filter { it.isNotEmpty() }.map { it[0] } }

    override fun part1(): Any {
        var sum = 0

        for (row in grid.indices) {
            var start = -1
            var acc = false
            var maybePartNo = ""
            for (col in grid[row].indices) {
                if (isDigit(grid[row][col])) {
                    acc = true
                    if (start == -1) {
                        start = col
                    }
                    maybePartNo += grid[row][col]
                } else if (acc) {
                    if (isPart(start, row, col - 1)) {
                        println("a: " + grid[row].subList(start, col))
                        sum += parseInt(maybePartNo)
                    }
                    acc = false
                    maybePartNo = ""
                    start = -1
                }
            }

            if (acc) {
                println("b: " + grid[row].subList(start, grid[row].size - 1))
                if (isPart(start, row, grid[row].size - 1)) {
                    sum += parseInt(maybePartNo)
                }
            }
        }

        return sum
    }

    private fun isPart(start: Int, row: Int, end: Int): Boolean {
        val chars = ArrayList<Char>()
        if (row > 0) {
            chars.addAll(grid[row - 1].subList(max(start - 1, 0), min(end + 2, grid[row].size - 1)))
        }
        if (row + 1 < grid.size - 1) {
            chars.addAll(grid[row + 1].subList(max(start - 1, 0), min(end + 2, grid[row].size - 1)))
        }
        if (start > 0) {
            chars.add(grid[row][start - 1])
        }
        if (end + 1 < grid[row].size - 1) {
            chars.add(grid[row][end + 1])
        }

        return chars.any {
            !isDigit(it) && it != '.'
        }
    }

    override fun part2(): Any {
        return -1
    }

}