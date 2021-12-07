import kotlin.math.abs

class DaySeven(file: String) : Project {
    private val subs = getLines(file).first().split(",").map { it.toInt() }
    private val uniqueSubs = subs.distinct()
    private val sortedUnique = uniqueSubs.sorted()

    override fun part1(): Any {
        val start = sortedUnique.first()
        val end = sortedUnique.last()

        val distances: ArrayList<Int> = ArrayList()

        for (i in start..end) {
            distances.add(subs.sumOf { abs(it - i) })
        }

        return distances.minOrNull() ?: -1
    }

    override fun part2(): Any {
        return -1
    }

}