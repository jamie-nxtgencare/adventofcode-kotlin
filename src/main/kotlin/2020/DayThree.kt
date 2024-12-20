@file:Suppress("PackageName")

package `2020`

import Project

class DayThree(file: String, isTest: Boolean = false) : Project(file, isTest) {
    private val grid = mapLettersPerLines(file) { it.map { it3 -> it3 == '#' }.toTypedArray() }.toTypedArray()

    override suspend fun part1(): Any {
        return traverse(3)
    }

    private fun traverse(xSlope: Int, ySlope: Int = 1): Int {
        var out = 0

        for (i in 0 until grid.size / ySlope) {
            val row = grid[i * ySlope]
            if (row[i * xSlope % row.size]) {
                out++
            }
        }
        return out
    }

    override suspend fun part2(): Any {
        return traverse(1) * traverse(3) * traverse(5) * traverse(7) * traverse(1, 2)
    }

}