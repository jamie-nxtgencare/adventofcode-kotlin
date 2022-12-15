@file:Suppress("PackageName")

package `2019`

import Project
import kotlin.math.ceil

class DayFourteen(file: String) : Project() {
    val reactions : Map<String, Reaction> = getReactions(file)

    private fun getReactions(file: String): Map<String, Reaction> {
        return mapFileLines(file) {
            val inputAndOutput = it.split(" => ")
            val inputs = inputAndOutput[0].split(",").map { it2 ->
                val inputStr = it2.trim().split(" ")
                CountAndType(inputStr[0].toLong(), inputStr[1])
            }

            val output = inputAndOutput[1].trim().split(" ")

            val reaction = Reaction(inputs, CountAndType(output[0].toLong(), output[1]))
            reaction
        }.groupBy{ it.output.type }.mapValues { it.value.first() }
    }

    private fun reduce(required: ArrayList<CountAndType>): Long {
        val extras = HashMap<String, Long>()

        while (!(required[0].type == "ORE" && required.size == 1)) {
            val nonOreRequirements = required.filter { it.type != "ORE" }.toHashSet()
            required.removeAll(nonOreRequirements)
            val newRequirementsMap = HashMap<String, Long>()

            extras.keys.forEach { extraKey ->
                nonOreRequirements.forEach { req ->
                    if (req.type == extraKey) {
                        while (extras[extraKey]!! > 0 && req.count > 0) {
                            extras[extraKey] = extras[extraKey]!! - 1
                            req.count--
                        }
                    }
                }
            }

            extras.filter { (_, value) -> value == 0L }.keys.forEach { extras.remove(it) }

            nonOreRequirements.forEach {
                val reaction: Reaction = reactions[it.type]!!
                val requiredCount = it.count

                val runTimes = ceil(requiredCount.toDouble() / reaction.output.count).toLong()
                val produces = runTimes * reaction.output.count
                val extra = produces - requiredCount

                reaction.inputs.forEach { input ->
                    val newCount = (newRequirementsMap[input.type] ?: 0) + (input.count * runTimes)
                    newRequirementsMap[input.type] = newCount
                }
                extras[it.type] = (extras[it.type] ?: 0) + extra

            }

            newRequirementsMap["ORE"] = (newRequirementsMap["ORE"] ?: 0) + if (required.size > 0) required[0].count else 0
            required.clear()

            newRequirementsMap.forEach { (key, value) ->
                required.add(CountAndType(value, key))
            }
        }

        return required[0].count
    }

    override fun part1(): Any {
        return getOre(1)
    }

    override fun part2(): Any {
        val goal = 1000000000000L

        var ore: Long
        var fuelCountMin = 0L
        var fuelCountMax = 100_000_000L
        var fuelCount: Long = -1

        while (fuelCountMax - fuelCountMin > 2) {
            fuelCount = (fuelCountMax + fuelCountMin) / 2
            ore = getOre(fuelCount)

            if (ore > goal) {
               fuelCountMax = fuelCount
            } else if (ore < goal) {
                fuelCountMin = fuelCount
            }

            println("$fuelCount $ore")
        }

        return fuelCount
    }

    private fun getOre(fuelCount: Long): Long {
        val required = ArrayList<CountAndType>()
        val requiredMap = HashMap<String, Long>()

        requiredMap["FUEL"] = fuelCount
        requiredMap.forEach { (key, value) -> required.add(CountAndType(value, key)) }

        return reduce(required)
    }

    class Reaction(val inputs: List<CountAndType>, val output: CountAndType) {
        override fun toString(): String {
            return "${inputs.joinToString(", ","","")} => $output"
        }
    }
    class CountAndType(var count: Long, val type: String) {
        override fun toString(): String {
            return "$count $type"
        }
    }
}

