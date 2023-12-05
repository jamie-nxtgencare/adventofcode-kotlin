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
    private val gearParts = HashMap<Pair<Int, Int>, MutableList<String>>()
    private var sum = 0

    init {
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
                        sum += parseInt(maybePartNo)
                        val part = grid[row].subList(start, col).joinToString("")
                        val attachedGears = isGearPart(start, row, col - 1)
                        attachedGears.forEach {
                            gearParts.computeIfAbsent(it) { mutableListOf() }.add(part)
                        }
                    }

                    acc = false
                    maybePartNo = ""
                    start = -1
                }
            }

            if (acc) {
                val col = grid[row].size
                if (isPart(start, row, col)) {
                    sum += parseInt(maybePartNo)
                }
                val part = grid[row].subList(start, col).joinToString("")
                val attachedGears = isGearPart(start, row, col - 1)
                attachedGears.forEach {
                    gearParts.computeIfAbsent(it) { mutableListOf() }.add(part)
                }
            }
        }
    }

    override fun part1(): Any {
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

    private fun isGearPart(start: Int, row: Int, end: Int): List<Pair<Int, Int>> {
        val gears = ArrayList<Pair<Int, Int>>()
        for (r in max(0, row - 1) .. min(row + 1, grid.size - 1)) {
            for (c in max(0, start - 1) .. min(end + 1, grid[r].size - 1)) {
                if (grid[r][c] == '*') {
                    gears.add(Pair(r, c))
                }
            }
        }
        return gears
    }

    override fun part2(): Any {
        val gearRatioComponents: Collection<MutableList<String>> = gearParts.filter { it.value.size == 2 }.values
        println(gearRatioComponents)

        return gearRatioComponents.sumOf { parseInt(it[0]) * parseInt(it[1]) }
    }

}