@file:Suppress("PackageName")

package `2022`

import Project
import java.lang.Integer.parseInt
import java.util.*
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sqrt
import checkCancellation

class DayNineteen(file: String, isTest: Boolean = false) : Project(file, isTest) {
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

        fun getCost(robot: String): Int {
            return costs.first { it.type == robot }.amount
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

    class Scenario(val robots: MutableMap<String, Int>, val inventory: MutableMap<String, Int>) {
        var minute = 1
        val buyRobots = mutableListOf<String>()

        companion object {
            fun clone(it: Scenario): Scenario {
                val scenario = Scenario(mutableMapOf(), mutableMapOf())
                scenario.minute = it.minute
                scenario.inventory.putAll(it.inventory)
                scenario.robots.putAll(it.robots)
                return scenario
            }
        }

        fun countRobots(produces: String): Int {
            return robots[produces] ?: 0
        }

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is Scenario) return false

            if (robots != other.robots) return false
            return inventory == other.inventory
        }

        override fun hashCode(): Int {
            return (countRobots("geode") * 547) +
                (countRobots("obsidian") * 293) +
                (countRobots("clay") * 73) +
                countRobots("ore")
        }

        override fun toString(): String {
            return "Scenario(minute=$minute, robots=$robots, inventory=$inventory)"
        }

        fun getStock(type: String): Int {
            return inventory[type] ?: 0
        }

    }

    override suspend fun part1(): Any {
        return doIt(24, bluePrints.size).sumOf { it.first * it.second }
    }

    override suspend fun part2(): Any {
        val second = doIt(32, 3)
        return second.reduce { a, b -> Pair(0, a.second * b.second) }.second
    }

    private suspend fun doIt(max: Int, maxBps: Int): List<Pair<Int, Int>> {
        return bluePrints.subList(0,min(maxBps,bluePrints.size)).map { bluePrint ->
            checkCancellation()

            val decisions = Stack<Scenario>()
            decisions.push(Scenario(mutableMapOf(Pair("ore", 1)), mutableMapOf()))
            var bestCountSoFar = 0
            val geodeMaker = bluePrint.getMaker("geode")
            val obsidianMaker = bluePrint.getMaker("obsidian")
            val clayMaker = bluePrint.getMaker("clay")
            val oreMaker = bluePrint.getMaker("ore")

            val maxObsidianRequired = geodeMaker.getCost("obsidian")
            val maxClayRequired = obsidianMaker.getCost("clay")
            val maxOreRequired = maxOf(geodeMaker.getCost("ore"), obsidianMaker.getCost("ore"), clayMaker.getCost("ore"), oreMaker.getCost("ore"))

            var skippedCount = 0
            var processed = 0

            while (decisions.isNotEmpty()) {
                if (processed % 10000 == 0) checkCancellation()

                val scenario = decisions.pop()
                val count = scenario.inventory["geode"] ?: 0
                val oldBestCount = bestCountSoFar
                bestCountSoFar = max(oldBestCount, count)

                if (oldBestCount != bestCountSoFar) {
                    if (Project.debug) println("New best count: $bestCountSoFar...")
                }

                if (scenario.minute <= max) {
                    checkCancellation()

                    val turnsRemain = (max - scenario.minute + 1).toDouble()

                    val obsidianCost = bluePrint.getMaker("obsidian").getCost("clay")
                    val geodeCost = bluePrint.getMaker("geode").getCost("obsidian")

                    var clayBotCount = scenario.countRobots("clay").toDouble() + 1
                    var clayStock = scenario.getStock("clay").toDouble() + clayBotCount
                    var turnsToWait = 0.0

                    while (clayStock < obsidianCost) {
                        clayStock += clayBotCount
                        clayBotCount++
                        turnsToWait++
                    }

                    var obsidianBotCount = scenario.countRobots("obisidian").toDouble() + 1
                    var obsidianStock = scenario.getStock("obsidian").toDouble() + obsidianBotCount

                    while (obsidianStock < geodeCost) {
                        obsidianStock += obsidianBotCount
                        obsidianBotCount++
                        turnsToWait++
                    }

                    val turnsWillRemain = max(turnsRemain - turnsToWait, 0.0)
                    val geodeBotCount = scenario.countRobots("geode").toDouble()
                    val geodeStock = scenario.getStock("geode")
                    val maxGeodes = geodeStock + (geodeBotCount * turnsRemain) + (0.5 * Math.pow(turnsWillRemain, 2.0) + turnsWillRemain - 0.5 * turnsWillRemain)

                    if (maxGeodes < bestCountSoFar) {
                        skippedCount++
                        continue
                    }

                    processed++

                    val newScenarios = mutableListOf<Scenario>()
                    newScenarios.add(scenario)

                    bluePrint.robotMakers.forEach { maker ->
                        val robotCount = scenario.countRobots(maker.produces)
                        val required = if (maker.produces == "obsidian") maxObsidianRequired else if (maker.produces == "clay") maxClayRequired else if (maker.produces == "ore") maxOreRequired else Int.MAX_VALUE
                        if (robotCount < required  && maker.canMakeWith(scenario.inventory)) {
                            val newScenario = Scenario.clone(scenario)
                            newScenario.buyRobots.add(maker.make(newScenario.inventory).produces)
                            newScenarios.add(newScenario)
                        }
                    }

                    if (newScenarios.size > 100) checkCancellation()
                    newScenarios.forEach {
                        it.robots.forEach { robot -> it.inventory[robot.key] = (it.inventory[robot.key] ?: 0) + robot.value }
                        it.buyRobots.forEach { robot -> it.robots[robot] = (it.robots[robot] ?: 0) + 1 }
                        it.buyRobots.clear()
                        it.minute++

                        decisions.push(it)
                    }
                }
            }
            // Remove or comment out "Best count" print
            Pair(bluePrint.label, bestCountSoFar)
        }
    }
}