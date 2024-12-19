@file:Suppress("PackageName")

package `2018`

import Project

class DayOne(file: String, isTest: Boolean = false) : Project(file, isTest) {
    private val freqs = mapFileLines(file) { it.toInt() }

    override suspend fun part1(): Any {
        return freqs.sum()
    }

    override suspend fun part2(): Any {
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