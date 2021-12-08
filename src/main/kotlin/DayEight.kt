class DayEight(file: String) : Project {
    private val inputOutput: List<List<String>> = mapFileLines(file) { it.split(" | ") }
    private val input = inputOutput.map { it.first().split(" ") }
    private val output = inputOutput.map { it.last().split(" ") }

    override fun part1(): Any {
        val oneFourSevenAndEight = output.flatten().filter { it.length == 2 || it.length == 4 || it.length == 3 || it.length == 7 }
        return oneFourSevenAndEight.size
    }

    override fun part2(): Any {
        return -1
    }

}