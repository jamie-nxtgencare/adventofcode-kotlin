@file:Suppress("PackageName")

package `2023`

import Project
import kotlin.math.abs

class DayEleven(val file: String) : Project() {
    private val space = getLines(file).map { it.split("").filter { it.isNotBlank() }.toMutableList() }.toMutableList()
    private val expandRows = space.mapIndexed { i, it -> if (!it.contains("#")) i else -1 }.filter { it >= 0 }
    private var expandCols = mutableListOf<Int>()
    private val galaxies = mutableListOf<Pair<Int, Int>>()

    init {
        for (i in space.first().indices) {
            var good = true
            for (row in space) {
                if (row[i] == "#") {
                    good = false
                    break
                }
            }
            expandCols.add(if (good) i else -1)
        }

        expandCols = expandCols.filter { it >= 0 }.toMutableList()

        expandCols.reversed().forEach { pos ->
            space.forEach { it.add(pos, ".") }
        }

        val blankRow = space.first { !it.contains("#") }

        expandRows.reversed().forEach {
            space.add(it, blankRow)
        }

        for (y in space.indices) {
            for (x in space[y].indices) {
                if (space[y][x] == "#") {
                    galaxies.add(Pair(x, y))
                }
            }
        }
    }



    override fun part1(): Any {
        var sum = 0
        for (i in 0 until galaxies.size) {
            for (j in i + 1 until galaxies.size) {
                sum += abs(galaxies[i].second - galaxies[j].second) + abs(galaxies[i].first - galaxies[j].first)
            }
        }

        return sum
    }

    override fun part2(): Any {
        return -1
    }
}