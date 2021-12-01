class DayOne(file: String) : Project {
    private val readings : List<Int> = mapFileLines(file) { it.toInt() }

    override fun part1(): Any {
        var increasing = 0
        var last = -1

        for (reading in readings) {
            if (last != -1) {
                if (reading > last) {
                    increasing++
                }
            }
            last = reading
        }
        return increasing
    }

    override fun part2(): Any {
        return -1
    }
}