import java.lang.Math.ceil

class DayFourteen(file: String) : Project {
    private val lines = getLines(file)
    val template = lines.first()
    var pairs = HashMap<String, String>()

    init {
        for (i in 2 until lines.size) {
            val pair = lines[i].split(" -> ")
            pairs[pair.first()] = pair.last()
        }
    }

    override fun part1(): Any {
        return doItNow(10)
    }

    override fun part2(): Any {
        //return -1
        return doItNow(40)
    }

    private fun doItNow(times: Int): Long {
        val counts = HashMap<String, Long>()
        val workingTemplate = StringBuilder(template)

        workingTemplate.split("").filter { it.isNotBlank() }.forEach {
            addOne(counts, it)
        }

        for (i in 0 until times) {
            runLoop(i, pairs, workingTemplate, counts)
        }

        val sorted = counts.values.sorted()

        return sorted.last() - sorted.first()
    }

    companion object {
        fun runLoop(iteration: Int, pairs: HashMap<String, String>, workingTemplate: StringBuilder, counts: java.util.HashMap<String, Long>) {
            var i = 0
            var length = workingTemplate.length
            while (i < length - 1) {
                val sub = workingTemplate.substring(i, i+2)
                val match = pairs[sub]
                if (match != null) {
                    workingTemplate.insert(i + 1, match)
                    addOne(counts, match)
                    length++
                    i++
                }
                i++
                println(iteration.toString() + ": --- " + ceil(i / length.toDouble() * 100.0) + "% ---")
            }
        }

        fun addOne(counts: HashMap<String, Long>, it: String) {
            val count = counts.computeIfAbsent(it) { 0L }
            counts[it] = count + 1L
        }
    }
}