@file:Suppress("PackageName")

package `2023`

import Project
import java.lang.Integer.min
import kotlin.math.abs
import kotlin.math.max

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
                val xStart:Int = min(galaxies[i].first, galaxies[j].first)
                val xEnd:Int = max(galaxies[i].first, galaxies[j].first)
                val yStart:Int = min(galaxies[i].second, galaxies[j].second)
                val yEnd:Int = max(galaxies[i].second, galaxies[j].second)

                val horizontalExpansion = expandCols.filter { (xStart..xEnd).contains(it) }.size
                val verticalExpansion = expandRows.filter { (yStart..yEnd).contains(it) }.size
                sum += abs(galaxies[i].second - galaxies[j].second) + abs(galaxies[i].first - galaxies[j].first) + horizontalExpansion + verticalExpansion
            }
        }

        return sum
    }

    override fun part2(): Any {
        return -1
    }
}