@file:Suppress("PackageName")

package `2019`

import Project
import kotlin.math.abs

class DayFourteen(file: String) : Project {
    val reactions : Map<String, Reaction> = getReactions(file)

    private fun getReactions(file: String): Map<String, Reaction> {
        return mapFileLines(file) {
            val inputAndOutput = it.split(" => ")
            val inputs = inputAndOutput[0].split(",").map { it2 ->
                val inputStr = it2.trim().split(" ")
                CountAndType(inputStr[0].toInt(), inputStr[1])
            }

            val output = inputAndOutput[1].trim().split(" ")

            val reaction = Reaction(inputs, CountAndType(output[0].toInt(), output[1]))
            reaction
        }.groupBy{ it.output.type }.mapValues { it.value.first() }
    }

    override fun part1(): Any {
        val fuel = reactions["FUEL"]!!
        val required = ArrayList<CountAndType>()
        val extras = HashMap<String, Int>()

        required.addAll(fuel.inputs)

        while (!(required[0].type == "ORE" && required.size == 1)) {
            val nonOreRequirements = required.filter { it.type != "ORE" }.toHashSet()
            required.removeAll(nonOreRequirements)
            val newRequirementsMap = HashMap<String, Int>()

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

            extras.filter { (_, value) -> value == 0 }.keys.forEach { extras.remove(it) }

            nonOreRequirements.forEach {
                println("Getting $it")
                val reaction: Reaction = reactions[it.type]!!
                var requiredCount = it.count
                while(requiredCount > 0) {
                    reaction.inputs.forEach { input ->
                        val newCount = (newRequirementsMap[input.type] ?: 0) + input.count
                        newRequirementsMap[input.type] = newCount
                    }
                    requiredCount -= reaction.output.count
                    if (requiredCount < 0) {
                        extras[it.type] = (extras[it.type] ?: 0) + abs(requiredCount)
                    }
                }
            }

            newRequirementsMap["ORE"] = (newRequirementsMap["ORE"] ?: 0) + if (required.size > 0) required[0].count else 0
            required.clear()

            newRequirementsMap.forEach { (key, value) ->
                required.add(CountAndType(value, key))
            }
        }

        return required[0].count
    }

    override fun part2(): Any {
        return -1
    }

    companion object {
        fun empty(): Reaction {
            return Reaction(ArrayList(), CountAndType(0, ""))
        }
    }

    class Reaction(val inputs: List<CountAndType>, val output: CountAndType) {
        override fun toString(): String {
            return "${inputs.joinToString(", ","","")} => $output"
        }
    }
    class CountAndType(var count: Int, val type: String) {
        override fun toString(): String {
            return "$count $type"
        }
    }
}

