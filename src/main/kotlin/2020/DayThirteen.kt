@file:Suppress("PackageName")

package `2020`

import Project

class DayThirteen(file: String, isTest: Boolean = false) : Project(file, isTest) {
    private val lines = getLines(file)
    private val timestamp = lines[0].toLong()
    private val busses = lines[1].split(",").map { if(it == "x") -1 else it.toLong() }
    private var ordered: List<Bus> = busses.mapIndexed { i, it -> Bus(i, it) }.sortedBy { it.mod }.filter { it.mod != -1L }

    override suspend fun part1(): Any {
        var i = timestamp - 1L
        var gotBus = false
        var bus = -1L

        while(!gotBus) {
            i++
            busses.forEach {
                if (it != -1L && i % it == 0L) {
                    bus = it
                    gotBus = true
                }
            }
        }

        return bus * (i - timestamp)
    }

    override suspend fun part2(): Any {

        var i = 1L
        var step = 1L

        for (bus in ordered.indices) {
            while (!valid(i, bus)) {
                i += step
            }
            step *= ordered[bus].mod
        }

        return i
    }

    private fun valid(i: Long, bus: Int): Boolean {
        return ((i + ordered[bus].remainder) % ordered[bus].mod) == 0L
    }

    class Bus(val remainder: Int, val mod: Long)
}