class DayTwo(file: String) : Project {
    private val instructions = mapFileLines(file) { Instruction(it) }

    override fun part1(): Any {
       var output = Pair(0, 0)
        instructions.forEach { output = it.apply(output) }
        return output.first * output.second
    }

    override fun part2(): Any {
        return -1
    }

}

class Instruction(it: String) {
    private val instructionStr = it.split(" ")
    private val instruction = instructionStr[0]
    private val amount: Int = instructionStr[1].toInt()

    fun apply(coord: Pair<Int, Int>): Pair<Int, Int> {
        return when(instruction) {
            "forward" -> Pair(coord.first.plus(amount), coord.second)
            "backward" -> Pair(coord.first.minus(amount), coord.second)
            "up" -> Pair(coord.first, coord.second.minus(amount))
            "down" -> Pair(coord.first, coord.second.plus(amount))
            else -> Pair(coord.first, coord.second)
        }
    }
}
