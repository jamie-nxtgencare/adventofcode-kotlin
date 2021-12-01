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
        var increasing = 0
        var curr3 = ArrayList<Int>()
        var last3 : ArrayList<Int>

        for (reading in readings) {
            if (curr3.size == 3) {
                last3 = ArrayList(curr3)
                curr3.add(reading)
                curr3.removeAt(0)

                val lastSum = last3.sum()
                val readingSum = curr3.sum()
                if (readingSum > lastSum) {
                    increasing++
                }
            } else {
                curr3.add(reading)
            }
        }
        return increasing
    }
}