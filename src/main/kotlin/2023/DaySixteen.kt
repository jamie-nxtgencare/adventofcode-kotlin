@file:Suppress("PackageName")

package `2023`

import Project
import java.time.Duration
import java.time.Instant
import kotlin.math.min

class DaySixteen(file: String) : Project() {
    private val valves = mapFileLines(file) { Valve(it) }
    private val valveMap = HashMap<String, Valve>()
    private val distances = HashMap<Valve, HashMap<Valve, Int>>()
    private val listDistances = HashMap<List<Valve>, Int>()
    private var time = Instant.now();
    private var valveCount = 0

    class Valve(it: String) {
        private val tokens = it.split(" ")
        private val rateS = tokens[4]
        val label = tokens[1]
        val rate = rateS.split("=")[1].split(";")[0].toInt()
        val leadsToS = tokens.subList(9, tokens.size).map { it.replace(",", "") }
        val leadsTo = ArrayList<Valve>()
        val goesBackTo = ArrayList<Valve>()

        override fun toString(): String {
            return "Valve(label='$label')"
        }
    }
    //Valve AA has flow rate=0; tunnels lead to valves DD, II, BB

    init {
        valves.forEach {
            valveMap[it.label] = it
        }

        valves.forEach {
            it.leadsToS.forEach { valveLabel ->
                it.leadsTo.add(valveMap[valveLabel]!!)
                valveMap[valveLabel]!!.goesBackTo.add(it)
            }
        }
    }

    class ValveStep(val valve: Valve, var step: Int)

    private fun getDistance(start: Valve, end: Valve): Int? {
        return (distances[start] ?: HashMap())[end]
    }

    private fun getDistance(list: List<Valve>): Int {
        if (listDistances[list] != null) {
            return listDistances[list]!!
        }

        if (list.size < 2) return 0
        val listCopy = ArrayList<Valve>()
        listCopy.addAll(list)

        var distance = 0

        while (listCopy.size > 1) {
            val last = listCopy.removeLast()
            val lastDistance = shortestPath(listCopy.last(), last)
            if (listDistances[listCopy] == null) {
                distance += lastDistance
            } else {
                val prefixDistance = listDistances[listCopy]!!
                listCopy.add(last)
                listDistances[listCopy] = prefixDistance + distance
                listCopy.removeLast()
            }
        }

        listDistances[list] = distance

        return distance
    }

    private fun shortestPath(start: Valve, end: Valve): Int {
        val distance = getDistance(start, end)
        if (distance != null) {
            return distance
        }

        val valveSteps = HashMap<Valve, ValveStep>()
        valves.forEach { valveSteps[it] = ValveStep(it, -1) }

        val visited = HashSet<ValveStep>()
        val toVisit = ArrayList<ValveStep>()
        val startValveSteps = valveSteps[start]!!
        startValveSteps.step = 0
        toVisit.add(startValveSteps)

        while (toVisit.isNotEmpty()) {
            val visiting = toVisit.removeFirst()
            visited.add(visiting)
            val neighbours = visiting.valve.leadsTo.map { valveSteps[it]!! }
            neighbours.filter { visited.contains(it) }.forEach { it.step = min(it.step, visiting.step) }
            neighbours.filter { !visited.contains(it) }.forEach {
                it.step = visiting.step + 1
                toVisit.add(it)
            }
        }

        val startMap = distances[start] ?: HashMap()
        startMap[end] = valveSteps[end]!!.step
        distances[start] = startMap

        return startMap[end]!!
    }

    override fun part1(): Any {
        val closedValves = ArrayList<Valve>()
        closedValves.addAll(valves.filter { it.rate > 0 })

        val pressure = 0
        val prev = valveMap["AA"]!!
        val minutes = 30
        return recurse(listOf(prev), pressure, prev, minutes, closedValves)
    }

    private fun recurse(path: List<Valve>, pressure: Int, prev: Valve, minutes: Int, closedValves: ArrayList<Valve>): Int {
        return closedValves.map { closedValve ->
            val remaining = ArrayList<Valve>()
            remaining.addAll(closedValves)
            remaining.remove(closedValve)
            val thisMinutes = minutes - shortestPath(prev, closedValve) - 1
            if (thisMinutes < 0) {
                pressure
            } else {
                val thisPressure = pressure + closedValve.rate * thisMinutes

                if (remaining.isEmpty()) {
                    thisPressure
                } else {
                    val newPath = ArrayList<Valve>()
                    newPath.addAll(path)
                    newPath.add(closedValve)

                    recurse(newPath, thisPressure, closedValve, thisMinutes, remaining)
                }
            }
        }.max()
    }

    private fun recurse2(
        pressure: Int, closedValves: ArrayList<Valve>, path: List<Valve>, prev: Valve, minutes: Int, path2: List<Valve>, prev2: Valve, minutes2: Int
    ): Int {
        return closedValves.map { closedValve ->
            if (path.size == 1) {
                println(closedValve)
                val nextTime = Instant.now()
                val seconds = Duration.between(time, nextTime).toSeconds()
                println("${seconds}s, ${(closedValves.size - valveCount++) * seconds}s remaining")
                time = nextTime
            }

            var outPressure = 0
            if (minutes >= minutes2) {
                val remaining = ArrayList<Valve>()
                remaining.addAll(closedValves)
                remaining.remove(closedValve)
                val thisMinutes = minutes - shortestPath(prev, closedValve) - 1
                if (thisMinutes < 0) {
                    outPressure = pressure
                    remaining.add(closedValve) // Add it back in again to try on the other side
                    if (minutes2 > 0) {
                        outPressure = recurse2(pressure, remaining, path, prev, 0, path2, prev2, minutes2)
                    }
                } else {
                    val thisPressure = pressure + closedValve.rate * thisMinutes

                    if (remaining.isEmpty()) {
                        outPressure = thisPressure
                    } else {
                        val newPath = ArrayList<Valve>()
                        newPath.addAll(path)
                        newPath.add(closedValve)

                        if (thisMinutes == 0 && minutes2 == 0) {
                            outPressure = thisPressure
                        } else {
                            outPressure = recurse2(thisPressure, remaining, newPath, closedValve, thisMinutes, path2, prev2, minutes2)
                        }
                    }
                }
            } else if (minutes < minutes2) {
                val remaining = ArrayList<Valve>()
                remaining.addAll(closedValves)
                remaining.remove(closedValve)
                val thisMinutes = minutes2 - shortestPath(prev2, closedValve) - 1
                if (thisMinutes < 0) {
                    outPressure= pressure
                    remaining.add(closedValve) // Add it back in again to try on the other side
                    if (minutes > 0) {
                        outPressure = recurse2(pressure, remaining, path, prev, minutes, path2, prev2, 0)
                    }
                } else {
                    val thisPressure = pressure + closedValve.rate * thisMinutes

                    if (remaining.isEmpty()) {
                        outPressure = thisPressure
                    } else {
                        val newPath = ArrayList<Valve>()
                        newPath.addAll(path)
                        newPath.add(closedValve)

                        if (thisMinutes == 0 && minutes == 0) {
                            outPressure = thisPressure
                        } else {
                            outPressure = recurse2(thisPressure, remaining, path, prev, minutes, newPath, closedValve, thisMinutes)
                        }
                    }
                }
            }

            outPressure
        }.max()
    }

    override fun part2(): Any {
        val closedValves = ArrayList<Valve>()
        closedValves.addAll(valves.filter { it.rate > 0 })

        val pressure = 0
        val prev = valveMap["AA"]!!
        val minutes = 26
        return recurse2(pressure, closedValves, listOf(prev), prev, minutes, listOf(prev), prev, minutes )
    }

}

