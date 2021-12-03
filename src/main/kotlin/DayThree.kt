import kotlin.math.roundToInt

class DayThree(file: String) : Project {
    private val input: List<List<Int>> = mapLettersPerLines(file) { it.map { it2 -> (it2 + "").toInt() }}

    override fun part1(): Any {
        val gammaSums: MutableList<Double> = ArrayList()

        for (row in input) {
            for (i in row.indices) {
                if (gammaSums.size < i + 1) gammaSums.add(0.0)
                gammaSums[i] = gammaSums[i] + row[i]
            }
        }

        for(i in gammaSums.indices) {
            gammaSums[i] = (gammaSums[i] / input.size.toDouble()).roundToInt().toDouble()
        }

        val gamma = gammaSums.map { it.toInt() }.joinToString("").toInt(2)
        val epsilon = gammaSums.map { if (it.toInt() == 1) 0 else 1 }.joinToString("").toInt(2)

        return gamma * epsilon
    }

    override fun part2(): Any {
        return -1
    }

}