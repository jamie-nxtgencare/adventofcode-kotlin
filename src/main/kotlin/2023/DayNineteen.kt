@file:Suppress("PackageName")

package `2023`

import Project
import java.lang.Integer.parseInt

class DayNineteen(file: String) : Project() {
    private val bluePrints: List<BluePrint> = mapFileLines(file) { BluePrint(it) }

    class BluePrintState(val bluePrint: BluePrint, val robots: MutableMap<String, Int>, val inputs: HashMap<String, Int>) {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is BluePrintState) return false

            if (bluePrint != other.bluePrint) return false
            if (robots != other.robots) return false
            if (inputs != other.inputs) return false

            return true
        }

        override fun hashCode(): Int {
            var result = bluePrint.hashCode()
            result = 31 * result + robots.hashCode()
            result = 31 * result + inputs.hashCode()
            return result
        }

        override fun toString(): String {
            return "BluePrintState(robots=${robots.values.sum()}, inputs=${inputs.size})"
        }

        fun score(): Int {
            return geodeScore() + obsidianScore() + clayScore() + oreScore()
        }

        private fun geodeScore(): Int {
            return get("geode") * 100000
        }

        private fun obsidianScore(): Int {
            return get("obsidian") * 100
        }

        private fun clayScore(): Int {
            return get("clay") * 10
        }

        private fun oreScore(): Int {
            return get("ore")
        }

        fun get(s: String): Int {
            return robots[s] ?: 0
        }

        fun getCount(s: String): Int {
            return inputs[s] ?: 0
        }

    }

    class BluePrint(line: String) {
        val lines = line.split(Regex("[.|:]")).filter { it.isNotBlank() }
        val label = lines[0].split(" ")[1].replace(":", "")
        private val robotMakers = lines.subList(1, lines.size).map { RobotMaker(it) }
        private val priorities = listOf(
            "geode",
            "obsidian",
            "clay",
            "ore"
        ).map { this.makerByType(it) }

        private fun makerByType(s: String): RobotMaker {
            return robotMakers.filter { it.produces == s }[0]
        }

        private fun canMake(inputs: HashMap<String, Int>, robotMaker: RobotMaker): Boolean {
            return robotMaker.costs.all { (inputs[it.type] ?: 0) > it.amount }
        }

        override fun toString(): String {
            return "BluePrint(label='$label', robotMakers=$robotMakers)"
        }

        fun pump(robots: MutableMap<String, Int>, inputs: HashMap<String, Int>): Set<Pair<MutableMap<String, Int>, HashMap<String, Int>>> {
            robots.forEach {
                val currentAmount = inputs[it.key] ?: 0
                inputs[it.key] = currentAmount + it.value
            }

            val options = priorities.filter { canMake(inputs, it) }.map {
                val newBots: MutableMap<String, Int> = mutableMapOf()
                newBots.putAll(robots)

                val newInputs = HashMap<String, Int>()
                newInputs.putAll(inputs)

                it.costs.forEach { i -> newInputs[i.type] = (newInputs[i.type] ?: 0) - i.amount }
                val robotCount = newBots[it.produces] ?: 0
                newBots[it.produces] = robotCount + 1

                Pair(newBots, newInputs)
            }.toMutableSet()

            options.add(Pair(robots, inputs))

            return options
        }
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

        override fun toString(): String {
            return "RobotMaker(produces='$produces', costs=$costs)"
        }
    }

    class Cost(val type: String, val amount: Int) {
        override fun toString(): String {
            return "Cost(type='$type', amount=$amount)"
        }
    }

    override fun part1(): Any {
        var bluePrintOutputs = bluePrints.map { setOf (BluePrintState(it, mutableMapOf(Pair("ore", 1)), HashMap())) }

        for (minute in 1..25) {
            bluePrintOutputs = bluePrintOutputs.map { bluePrint ->
                bluePrint.flatMap {  state ->
                    val out = state.bluePrint.pump(state.robots, state.inputs)
                    out.map { o -> BluePrintState(state.bluePrint, o.first, o.second) }
                }.toMutableSet()
            }

            bluePrintOutputs = bluePrintOutputs.map { it.sortedByDescending { s -> s.score() }.take(1).toMutableSet()}
        }

        val map = bluePrintOutputs.map { it.maxOf { inputs -> inputs.getCount("geode") * inputs.bluePrint.label.toInt() } }

        return map.sum()
    }

    override fun part2(): Any {
        return -1
    }

}