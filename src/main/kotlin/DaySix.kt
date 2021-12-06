class DaySix(file: String) : Project {
    private val input: List<Int> = getLines(file).first().split(",").map { it.toInt() }
    private val ageCounts = Array(9) { 0 }

    init {
        input.forEach { ageCounts[it]++ }
    }

    override fun part1(): Any {
        val days = 80
        for (i in days - 1 downTo 0) {
            advanceDay()
        }

        return ageCounts.sum()
    }

    override fun part2(): Any {
        return -1
    }

    fun advanceDay() {
        var tmp = 0
        for (i in ageCounts.size downTo 0) {
            if (i > 0) {
                val tmp2 = ageCounts[i - 1]
                ageCounts[i - 1] = tmp
                tmp = tmp2
            } else {
                ageCounts[6] += tmp
                ageCounts[8] += tmp
            }
        }
    }
}