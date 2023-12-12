@file:Suppress("PackageName")

package `2022`

import Project
import java.lang.Integer.parseInt
import kotlin.math.ceil

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

        fun getCost(robot: String): Double {
            return costs.first { it.type == robot }.amount.toDouble()
        }

        fun canMakeWith(inventory: MutableMap<String, Int>): Boolean {
            val depletableInventory = mutableMapOf<String, Int>()
            depletableInventory.putAll(inventory)

            costs.forEach {
                depletableInventory[it.type] = (depletableInventory[it.type] ?: 0) - it.amount
            }

            return depletableInventory.all { it.value >= 0 }
        }

        fun shouldMakeWith(scenario: Scenario, biggerBots: List<RobotMaker>): Boolean {
            if (!canMakeWith(scenario.inventory)) {
                return false
            }

            val purchaseScenario = Scenario.clone(scenario)
            make(purchaseScenario.inventory)

            val aaa = biggerBots.map { nextMaker ->
                // Does this prevent me from making ANYTHING bigger when I can?
                val nonOreRequirement = if (nextMaker.costs.count { it.type != "ore" } > 0) nextMaker.costs.first { it.type != "ore" }.type else "ore"
                val required = nextMaker.getCost(nonOreRequirement) - purchaseScenario.getStock(nonOreRequirement).toDouble()
                val morePerMinute = purchaseScenario.robotCount(nonOreRequirement).toDouble()
                val minutesUntilPurchase = if (purchaseScenario.robotCount(nonOreRequirement) == 0) Double.MAX_VALUE else ceil(required / morePerMinute)

                val futureOre = purchaseScenario.getStock("ore") + purchaseScenario.robotCount("ore") * minutesUntilPurchase
                val notPreventingBiggerBuy = futureOre >= nextMaker.getCost("ore")

                if (!notPreventingBiggerBuy) {
                    println("Not making $produces because that stops use from making ${nextMaker.produces} in $minutesUntilPurchase minutes")
                }

                notPreventingBiggerBuy
            }

            return aaa.all { it }
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

        fun robotCount(robot: String): Int {
            return robots.filter { it.produces == robot }.size
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
            return "Scenario(minute=$minute, robots=$robots, inventory=$inventory)"
        }

        fun getStock(type: String): Int {
            return inventory[type] ?: 0
        }

    }

    override fun part1(): Any {
        return bluePrints.sumOf { bluePrint ->
            /*
            If you can buy a geode, buy one
            If you can buy a obsidian:
                Figure out when you can buy your next geode.
                Does buying an obsidian now stop you from having enough ore to buy a geode then?
                    No: Buy obsidian
                    Yes: Don't buy obsidian
            If you can buy a clay:
                Figure out when you can buy your next obsidian.
                Does buying a clay now stop you from having enough ore to buy an obsidian then?
                    No: Buy clay
                    Yes: Don't buy clay
            If you can buy an ore:
                Figure out when you can buy your next clay.
                Does buying an ore now stop you from having enough ore to buy a clay then?
                    No: Buy ore
                    Yes: Don't buy ore
             */
            val scenario = Scenario(mutableListOf(Robot("ore")), mutableMapOf())
            val geodeMaker = bluePrint.getMaker("geode")
            val obsidianMaker = bluePrint.getMaker("obsidian")
            val clayMaker = bluePrint.getMaker("clay")
            val oreMaker = bluePrint.getMaker("ore")

            for (minute in 1 .. 24) {
                println("== Minute $minute ==")
                if (geodeMaker.canMakeWith(scenario.inventory)) {
                    println("Spend ${geodeMaker.getCost("ore")} ore and ${geodeMaker.getCost("obsidian")} obsidian to start building an geode-cracking robot")
                    scenario.buyRobots.add(geodeMaker.make(scenario.inventory))
                } else if (obsidianMaker.shouldMakeWith(scenario, listOf(geodeMaker))) {
                    println("Spend ${obsidianMaker.getCost("ore")} ore and ${obsidianMaker.getCost("clay")} clay to start building an obsidian-collecting robot")
                    scenario.buyRobots.add(obsidianMaker.make(scenario.inventory))
                } else if (clayMaker.shouldMakeWith(scenario, listOf(geodeMaker, obsidianMaker))) {
                    println("Spend ${clayMaker.getCost("ore")} ore to start building an clay-collecting robot")
                    scenario.buyRobots.add(clayMaker.make(scenario.inventory))
                } else if (oreMaker.shouldMakeWith(scenario, listOf(geodeMaker, obsidianMaker, clayMaker))) {
                    println("Spend ${oreMaker.getCost("ore")} ore to start building an ore-collecting robot")
                    scenario.buyRobots.add(oreMaker.make(scenario.inventory))
                }

                scenario.robots.map { it.produces }.distinct().forEach { produces ->
                    val increase = scenario.robots.count { it.produces == produces }
                    scenario.inventory[produces] = (scenario.inventory[produces] ?: 0) + increase
                    println("$increase $produces-collecting robots collect $increase $produces; you now have ${scenario.inventory[produces]} $produces.")
                }
                scenario.robots.addAll(scenario.buyRobots)
                scenario.buyRobots.clear()
                scenario.minute++
                println()
            }


            bluePrint.label * scenario.getStock("geode")
        }
    }

    override fun part2(): Any {
        return -1
    }

}