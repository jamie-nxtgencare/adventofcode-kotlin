import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class DayFourteen(file: String) : Project {
    val lines = getLines(file)
    val template = lines.first()
    var pairs = ArrayList<Pair<String, String>>()

    init {
        for (i in 2 until lines.size) {
            val pair = lines[i].split(" -> ")
            pairs.add(Pair(pair.first(), pair.last()))
        }
    }

    override fun part1(): Any {
        return doItNow(10)
    }

    override fun part2(): Any {
        return doItNow(40)
    }

    private fun doItNow(times: Int): Long {
        val counts = HashMap<String, Long>()
        val workingTemplate = StringBuilder(template)

        workingTemplate.split("").filter { it.isNotBlank() }.forEach {
            addOne(counts, it)
        }

        for (i in 0 until times) {
            runLoop(pairs, workingTemplate, counts)
            println(i)
        }

        val sorted = counts.values.sorted()

        return sorted.last() - sorted.first()
    }

    companion object {
        fun runLoop(pairs: ArrayList<Pair<String, String>>, workingTemplate: StringBuilder, counts: java.util.HashMap<String, Long>) {
            val insertionLocations = ArrayList<Pair<Int, String>>()
            for (i in 0..workingTemplate.length - 2) {
                val sub = workingTemplate.substring(i,i+2)
                pairs.forEach {
                    if (it.first == sub) {
                        insertionLocations.add(Pair(i + 1, it.second))
                    }
                }
            }
            val sorted = insertionLocations.sortedBy { it.first }

            for (i in sorted.indices) {
                workingTemplate.insert(sorted[i].first + i, sorted[i].second)
                addOne(counts, sorted[i].second)
            }
        }

        fun addOne(counts: HashMap<String, Long>, it: String) {
            val count = counts.computeIfAbsent(it) { 0L }
            counts[it] = count + 1L
        }
    }
}