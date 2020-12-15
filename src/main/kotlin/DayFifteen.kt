class DayFifteen(file: String): Project {
    val debug = false
    val input = getLines(file)[0].split(",").map { it.toInt() }

    override fun part1(): Any {
        val lastSeenMap = HashMap<Int, Int>()

        for (i in 0..input.size-2) { lastSeenMap[input[i]] = i }
        var lastNum = input[input.size - 1]

        var i = input.size - 1
        var j = i + 2
        while (j <= 2020) { /* +3 = 1 for 0 index, 1 for reading "last spoken" each i, 1 for "until" */

            when (val maybeLastNum = lastSeenMap[lastNum]) {
                null -> {
                    lastSeenMap[lastNum] = i
                    lastNum = 0
                }
                else -> {
                    lastSeenMap[lastNum] = i
                    lastNum = i - maybeLastNum
                }
            }

            if (debug) {
                println("$j: $lastNum")
            }

            i++
            j++
        }

        return lastNum
    }

    override fun part2(): Any {
        return -1
    }
}