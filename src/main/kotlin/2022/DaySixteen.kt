@file:Suppress("PackageName")

package `2022`

import Project

class DaySixteen(file: String) : Project() {
    val valves = mapFileLines(file) { Valve(it) }
    val valveMap = HashMap<String, Valve>()

    class Valve(it: String) {
        private val tokens = it.split(" ")
        private val rateS = tokens[4]
        val label = tokens[1]
        val rate = rateS.split("=")[1].split(";")[0].toInt()
        val leadsToS = tokens.subList(9, tokens.size).map { it.replace(",", "") }
        val leadsTo = ArrayList<Valve>()

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
            }
        }
    }

    class ValveContext(val valve: Valve, val minute: Int, val pressureReleased: Int, val openValves: ArrayList<Valve> = ArrayList(), val visited: HashSet<ValveContext> = HashSet()) {
        override fun toString(): String {
            return "ValveContext(valve=$valve, minute=$minute, pressureReleased=$pressureReleased)"
        }
    }

    override fun part1(): Any {
        val toVisit = ArrayList<ValveContext>()
        val finalVisitedContexts = HashSet<Int>()

        toVisit.add(ValveContext(valveMap["AA"]!!, 1, 0))

        while (toVisit.isNotEmpty()) {
            val visiting = toVisit.removeFirst()
            visiting.visited.add(visiting)

            if (visiting.minute == 30) {
                finalVisitedContexts.add(visiting.pressureReleased)
            } else {
                var endOfPath = true
                visiting.valve.leadsTo.map {
                    val context = ValveContext(it, visiting.minute + 1, visiting.pressureReleased)
                    context.openValves.addAll(visiting.openValves)
                    context.visited.addAll(visiting.visited)
                    context
                }.filter {
                    val future = visiting.visited.filter { a -> a.minute >= it.minute }
                    val pressures = future.ifEmpty { listOf(ValveContext(valveMap["AA"]!!, 0, 0)) }
                    it.pressureReleased >= pressures.maxOf { a -> a.pressureReleased }
                }.forEach {
                    toVisit.add(it)
                    endOfPath = false
                }

                if (!visiting.openValves.contains(visiting.valve)) {
                    val visit = ValveContext(
                        visiting.valve,
                        visiting.minute + 1,
                        visiting.pressureReleased + (visiting.valve.rate * (30 - visiting.minute + 1))
                    )
                    visit.openValves.addAll(visiting.openValves)
                    visit.visited.addAll(visiting.visited)
                    visit.openValves.add(visiting.valve)

                    toVisit.add(visit)
                    endOfPath = false
                }

                if (endOfPath) {
                    finalVisitedContexts.add(visiting.pressureReleased)
                }
            }
            toVisit.sortByDescending { it.pressureReleased }

        }

        return finalVisitedContexts.maxOf { it }
    }

    override fun part2(): Any {
        return -1
    }

}