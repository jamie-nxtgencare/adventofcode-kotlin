
class DaySix(file: String) : Project {
    private val input: List<Int> = getLines(file).first().split(",").map { it.toInt() }
    private val ageCounts: Array<Long> = Array(9) { 0L }
    private val initialAgeCounts: Array<Long> = Array(9) { 0L }

    init {
        input.forEach { ageCounts[it]++ }
        ageCounts.copyInto(initialAgeCounts)
    }

    override fun part1(): Any {
        return getAnswer(80)
    }

    override fun part2(): Any {
        initialAgeCounts.copyInto(ageCounts)
        return getAnswer(256)
    }

    private fun getAnswer(days: Int): Long {
        for (i in days - 1 downTo 0) {
            advanceDay()
        }

        return ageCounts.sum()
    }

    fun advanceDay() {
        var tmp = 0L
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