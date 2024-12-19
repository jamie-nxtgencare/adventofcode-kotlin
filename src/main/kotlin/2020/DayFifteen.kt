@file:Suppress("PackageName")

package `2020`

import Project
import checkCancellation

class DayFifteen(file: String, isTest: Boolean = false) : Project(file, isTest) {
    private val input = getLines(file)[0].split(",").map { it.toInt() }

    override suspend fun part1(): Any {
        return speak(2020)
    }

    override suspend fun part2(): Any {
        return speak(30_000_000)
    }

    private suspend fun speak(number: Int): Int {
        val lastSeenMap = arrayOfNulls<Int>(30_000_000)

        for (i in 0..input.size-2) { lastSeenMap[input[i]] = i }
        var lastNum = input[input.size - 1]

        var i = input.size - 1
        while (i < number - 1) {
            checkCancellation()
            val oldLast = lastNum
            lastNum = lastSeenMap[lastNum]?.let { i - it } ?: 0
            lastSeenMap[oldLast] = i++
        }

        return lastNum
    }
}