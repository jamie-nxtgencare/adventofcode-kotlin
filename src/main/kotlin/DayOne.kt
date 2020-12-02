class DayOne(file: String) {
    private val map = getMap(file)

    fun part1(): Int {
        for (entry in map) {
            val a = entry.key
            val b = 2020 - a
            if (map.contains(b)) {
                return a * b
            }
        }

        return -1
    }

    fun part2(): Int {
        for (e1 in map) {
            for (e2 in map) {
                val a = e1.key
                val b = e2.key
                val c = 2020 - (a + b)
                if (map.contains(c)) {
                    return a * b * c
                }
            }
        }

        return -1
    }

    private fun getMap(file: String): Map<Int, Int> {
        return getLines(file).map { l -> Integer.parseInt(l) to 1 }.toMap()
    }
}