@file:Suppress("PackageName")
package `2021`

import Project

class DayOne(file: String, isTest: Boolean = false) : Project(file, isTest) {
    private val readings : List<Int> = mapFileLines(file) { it.toInt() }

    override suspend fun part1(): Any {
        var increasing = 0
        var last = -1

        for (reading in readings) {
            if (last != -1) {
                if (reading > last) {
                    increasing++
                }
            }
            last = reading
        }
        return increasing
    }

    override suspend fun part2(): Any {
        var increasing = 0;
        for (i in 3 until readings.size) {
            if (readings[i] > readings[i-3]) {
                increasing++
            }
        }
        return increasing
    }
}