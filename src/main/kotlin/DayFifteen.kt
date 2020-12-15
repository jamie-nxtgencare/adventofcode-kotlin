class DayFifteen(file: String): Project {
    val debug = false
    val input = getLines(file)[0].split(",").map { it.toInt() }

    override fun part1(): Any {
        return speak(2020)
    }

    override fun part2(): Any {
        return speak(30000000)
    }

    private fun speak(number: Int): Int {
        val lastSeenMap = HashMap<Int, Int>()

        for (i in 0..input.size-2) { lastSeenMap[input[i]] = i }
        var lastNum = input[input.size - 1]

        var i = input.size - 1
        var j = i + 2
        while (j <= number) { /* +3 = 1 for 0 index, 1 for reading "last spoken" each i, 1 for "until" */
            val oldLast = lastNum
            lastNum = when (val maybeLastNum = lastSeenMap[lastNum]) {
                null -> 0
                else -> i - maybeLastNum
            }
            lastSeenMap[oldLast] = i

            if (debug) {
                println("$j: $lastNum")
            }

            i++
            j++
        }

        return lastNum
    }
}