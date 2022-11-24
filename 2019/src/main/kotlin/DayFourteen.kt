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
        }.groupBy{ it.outputs.type }.mapValues { it.value.first() }
    }

    override fun part1(): Any {
        val output = (reactions["FUEL"] ?: empty()).decompose(reactions)
        return output["ORE"] ?: 0
    }

    override fun part2(): Any {
        return -1
    }

    companion object {
        fun empty(): Reaction {
            return Reaction(ArrayList(), CountAndType(0, ""))
        }
    }

    class Reaction(val inputs: List<CountAndType>, val outputs: CountAndType) {
        fun decompose(reactions : Map<String, Reaction>): HashMap<String, Int> {
            val outputReactions : HashMap<String, Int> = HashMap()
            for (input in inputs) {
                if (input.type == "ORE") {
                    outputReactions.put(input.type, input.count)
                    continue
                }

                val newReactions : HashMap<String, Int> = (reactions[input.type] ?: empty()).decompose(reactions)
                outputReactions.putAll(newReactions)
            }
            return outputReactions
        }
    }

    class CountAndType(var count: Int, val type: String)
}

