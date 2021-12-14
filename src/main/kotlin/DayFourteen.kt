class DayFourteen(file: String) : Project {
    val lines = getLines(file)
    val template = lines.first()
    var pairs = ArrayList<Pair<Regex, String>>()

    init {
        for (i in 2 until lines.size) {
            val pair = lines[i].split(" -> ")
            val first = pair.first().substring(0,1)
            val second = pair.first().substring(1,2)
            val regex = if (first == second) String.format("(?=(%s{2,}))", first) else pair.first()
            pairs.add(Pair(Regex(regex), pair.last()))
        }
    }

    override fun part1(): Any {
        val counts = HashMap<String, Int>()
        val workingTemplate = StringBuilder(template)

        workingTemplate.split("").filter { it.isNotBlank() }.forEach {
            addOne(counts, it)
        }

        for (i in 0..9) {
            runLoop(pairs, workingTemplate, counts)
        }

        val sorted = counts.values.sorted()

        return sorted.last() - sorted.first()
    }

    override fun part2(): Any {
        return -1
    }


    companion object {
        fun runLoop(pairs: ArrayList<Pair<Regex, String>>, workingTemplate: StringBuilder, counts: java.util.HashMap<String, Int>) {
            val insertionLocations = ArrayList<Pair<Int, String>>()
            pairs.forEach {
                it.first.findAll(workingTemplate).forEach { match -> insertionLocations.add(Pair(match.range.first + 1, it.second)) }
            }
            val sorted = insertionLocations.sortedBy { it.first }

            for (i in sorted.indices) {
                workingTemplate.insert(sorted[i].first + i, sorted[i].second)
                addOne(counts, sorted[i].second)
            }
        }

        fun addOne(counts: HashMap<String, Int>, it: String) {
            val count = counts.computeIfAbsent(it) { 0 }
            counts[it] = count + 1
        }
    }
}