@file:Suppress("PackageName")

package `2020`

import Project

class DayFifteen(file: String): Project() {
    private val input = getLines(file)[0].split(",").map { it.toInt() }

    override fun part1(): Any {
        return speak(2020)
    }

    override fun part2(): Any {
        return speak(30_000_000)
    }

    private fun speak(number: Int): Int {
        val lastSeenMap = arrayOfNulls<Int>(30_000_000)

        for (i in 0..input.size-2) { lastSeenMap[input[i]] = i }
        var lastNum = input[input.size - 1]

        var i = input.size - 1
        while (i < number - 1) {
            val oldLast = lastNum
            lastNum = lastSeenMap[lastNum]?.let { i - it } ?: 0
            lastSeenMap[oldLast] = i++
        }

        return lastNum
    }
}