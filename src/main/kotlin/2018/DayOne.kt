@file:Suppress("PackageName")

package `2018`

import Project

class DayOne(file: String) : Project {
    private val freqs = mapFileLines(file) { it.toInt() }

    override fun part1(): Any {
        return freqs.sum()
    }

    override fun part2(): Any {
        val map = HashMap<Int, Int>()
        var sum = 0
        var i = 0

        while (map[sum] != 2) {
            i = if(i >= freqs.size) 0 else i

            sum += freqs[i]
            map[sum] = (map[sum] ?: 0) + 1

            i++
        }

        return sum
    }
}