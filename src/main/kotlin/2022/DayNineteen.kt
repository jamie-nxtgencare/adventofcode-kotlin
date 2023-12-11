@file:Suppress("PackageName")

package `2022`

import Project
import java.lang.Integer.parseInt
import java.util.*
import kotlin.math.max

class DayNineteen(file: String) : Project() {
    private val bluePrints: List<BluePrint> = mapFileLines(file) { BluePrint(it) }

    class BluePrint(line: String) {

        fun getMaker(robot: String): RobotMaker {
            return robotMakers.first { it.produces == robot }
        }

        val lines = line.split(Regex("[.|:]")).filter { it.isNotBlank() }
        val label = parseInt(lines[0].split(" ")[1].replace(":", ""))
        val robotMakers = lines.subList(1, lines.size).map { RobotMaker(it) }.reversed()
    }

    class RobotMaker(s: String) {
        var produces: String
        var costs: List<Cost>

        init {
            val tokens = s.split("costs")
            produces = tokens[0].trim().split(" ")[1]
            val costStrings = tokens[1].trim().split(" and ")
            costs = costStrings.map {
                val costsTokens = it.split(" ")
                Cost(costsTokens[1].trim('.'), parseInt(costsTokens[0]))
            }
        }

        fun make(inventory: MutableMap<String, Int>): Robot {
            costs.forEach { inventory[it.type] = (inventory[it.type] ?: 0) - it.amount }
            return Robot(produces)
        }

        override fun toString(): String {
            return "RobotMaker(produces='$produces', costs=$costs)"
        }

        fun canMakeWith(inventory: MutableMap<String, Int>): Boolean {
            val depletableInventory = mutableMapOf<String, Int>()
            depletableInventory.putAll(inventory)

            costs.forEach {
                depletableInventory[it.type] = (depletableInventory[it.type] ?: 0) - it.amount
            }

            return depletableInventory.all { it.value >= 0 }
        }
    }

    class Robot(val produces: String) {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is Robot) return false

            return produces == other.produces
        }

        override fun hashCode(): Int {
            return produces.hashCode()
        }

        override fun toString(): String {
            return "Robot(produces='$produces')"
        }


    }

    class Cost(val type: String, val amount: Int) {
        override fun toString(): String {
            return "Cost(type='$type', amount=$amount)"
        }
    }

    class Scenario(val robots: MutableList<Robot>, val inventory: MutableMap<String, Int>) {
        var minute = 1
        val buyRobots = mutableListOf<Robot>()
        var maxGeodeValue = updateMaxValue("geode", 24)

        fun updateMaxValue(type: String, minutesRemaining: Int): Double {
            return (inventory[type]?.toDouble() ?: 0.0) +
                    (robots.count { it.produces == type} * minutesRemaining) +
                    0.5 * Math.pow(minutesRemaining.toDouble(), 2.0) - minutesRemaining.toDouble() * 0.5
        }

        companion object {
            fun clone(it: Scenario): Scenario {
                val scenario = Scenario(mutableListOf(), mutableMapOf())
                scenario.minute = it.minute
                scenario.inventory.putAll(it.inventory)
                scenario.robots.addAll(it.robots)
                return scenario
            }
        }

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is Scenario) return false

            if (robots != other.robots) return false
            return inventory == other.inventory
        }

        override fun hashCode(): Int {
            var result = robots.hashCode()
            result = 31 * result + inventory.hashCode()
            return result
        }

        override fun toString(): String {
            return "Scenario(robots=$robots, inventory=$inventory)"
        }

    }

    override fun part1(): Any {
        return bluePrints.sumOf { bluePrint ->
            val decisions = Stack<Scenario>()
            decisions.push(Scenario(mutableListOf(Robot("ore")), mutableMapOf()))
            var bestCountSoFar = 0

            while (decisions.isNotEmpty()) {
                val scenario = decisions.pop()
                val count = scenario.inventory["geode"] ?: 0
                val oldBestCount = bestCountSoFar
                bestCountSoFar = max(oldBestCount, count)

                if (oldBestCount != bestCountSoFar) {
                    println("New best count: $bestCountSoFar")
                }

                if (scenario.minute <= 24) {
                    val newScenarios = mutableSetOf<Scenario>()

                    // do nothing
                    newScenarios.add(scenario)

                    var newCount = 0
                    while (newCount != newScenarios.size) {
                        newCount = newScenarios.size
                        bluePrint.robotMakers.forEach { maker ->
                            if (maker.canMakeWith(scenario.inventory)) {
                                val newScenario = Scenario.clone(scenario) // Make a scenario where we made it
                                newScenario.buyRobots.add(maker.make(newScenario.inventory))
                                newScenarios.add(newScenario)

                                val newScenario2 = Scenario.clone(scenario) // Make a scenario where we didn't
                                newScenarios.add(newScenario2)
                            }
                        }
                    }

                    newScenarios.forEach {
                        it.robots.forEach { robot -> it.inventory[robot.produces] = (it.inventory[robot.produces] ?: 0) + 1 }
                        it.robots.addAll(it.buyRobots)
                        it.buyRobots.clear()
                        it.robots.sortBy { it.produces }
                        it.minute++
                        it.maxGeodeValue = it.updateMaxValue("geode",  25 - it.minute)

                        if (it.maxGeodeValue >= bestCountSoFar) {
                            decisions.push(it)
                        }
                    }
                }
            }
            println("Best count $bestCountSoFar")
            bluePrint.label * bestCountSoFar
        }
    }

    override fun part2(): Any {
        return -1
    }

}