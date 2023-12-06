@file:Suppress("PackageName")

package `2023`

import Project
import java.lang.Integer.parseInt

class DaySix(file: String) : Project() {
    private val lines = getLines(file)
    private val timeArr = lines[0].replace(Regex("Time:\\s*"), "").split(" ")
    private val timeStr = timeArr.filter { it.isNotBlank() }
    private val times = timeStr.map { parseInt(it) }
    private val distanceArr = lines[1].replace(Regex("Distance:\\s*"), "")
    private val distanceStr = distanceArr.split(" ").filter { it.isNotBlank() }
    private val distances = distanceStr.map { parseInt(it) }

    override fun part1(): Any {
        return times.mapIndexed { i, time ->
            val distance = distances[i]
            var waysToWin = 0

            for (hold in 0 until time) {
                val newDistance = hold * (time - hold)
                if (newDistance > distance) {
                    waysToWin++
                }
            }

            waysToWin
        }.reduce { a, b -> a * b }
    }

    override fun part2(): Any {
        return -1
    }

}