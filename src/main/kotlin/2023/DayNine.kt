@file:Suppress("PackageName")

package `2023`

import Project
import java.lang.Integer.parseInt

class DayNine(file: String) : Project() {
    val lines = getLines(file).map { it.split(" ").map { parseInt(it)}.toMutableList() }

    override fun part1(): Any {
        return lines.sumOf { history ->
            val progressions = mutableListOf(history)
            while (progressions.last().any { it != 0 }) {
                val next = mutableListOf<Int>()
                val last = progressions.last()
                for (i in 0..last.size - 2) {
                    val x1 = last[i]
                    val x2 = last[i + 1]
                    next.add(x2 - x1)
                }
                progressions.add(next)
            }

            val reversed = progressions.reversed()
            for (i in 0..reversed.size - 2) {
                reversed[i + 1].add(reversed[i + 1].last() + reversed[i].last())
            }

            reversed.last().last()
        }
    }

    override fun part2(): Any {
        return -1
    }
}