import kotlin.math.abs

class DaySeven(file: String) : Project {
    private val subs = getLines(file).first().split(",").map { it.toInt() }
    private val uniqueSubs = subs.distinct()
    private val sortedUnique = uniqueSubs.sorted()
    private val start = sortedUnique.first()
    private val end = sortedUnique.last()
    private val iMemo = HashMap<Int, Int>()

    override fun part1(): Any {
        return getDistance(1)
    }

    override fun part2(): Any {
        return getDistance(2)
    }

    private fun getDistance(part: Int): Int {
        val distances: ArrayList<Int> = ArrayList()

        for (i in start..end) {
            distances.add(subs.sumOf { if (part == 1) abs(it - i) else getI(abs(it - i)) })
        }

        return distances.minOrNull() ?: -1
    }

    private fun getI(i: Int): Int {
        val orDefault = iMemo.getOrDefault(i, null)
        if (orDefault != null) {
            return orDefault
        }

        val curr = if(i == 0) 0 else i + getI(i-1)

        iMemo[i] = curr

        return curr
    }
}