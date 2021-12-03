import kotlin.math.roundToInt

class DayThree(file: String) : Project {
    private val input: List<List<Int>> = mapLettersPerLines(file) { it.map { it2 -> (it2 + "").toInt() }}
    private var mostCommonBits: List<Int> = ArrayList()
    private var leastCommonBits: List<Int> = ArrayList()

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

        this.mostCommonBits = gammaSums.map { it.toInt() }
        this.leastCommonBits = gammaSums.map { if (it.toInt() == 1) 0 else 1 }

        val gamma = mostCommonBits.joinToString("").toInt(2)
        val epsilon = leastCommonBits.joinToString("").toInt(2)
        return gamma * epsilon
    }

    override fun part2(): Any {
        val oxygenCandidates: MutableList<List<Int>> = ArrayList(input)
        var mostCommonSubStr = ""
        var subStringSize = 1;

        while (oxygenCandidates.size > 1) {
            val toRemove = ArrayList<List<Int>>()

            var sum = 0.0
            for (row in oxygenCandidates) {
                sum += row[subStringSize - 1]
            }

            val mostCommonBit = (sum / oxygenCandidates.size.toDouble()).roundToInt()
            mostCommonSubStr += mostCommonBit

            for (candidate in oxygenCandidates) {
                val candidateSubstr = candidate.joinToString("").substring(0, subStringSize)
                if (sum == 0.5 && !candidateSubstr.endsWith("1")) {
                    toRemove.add(candidate)
                } else if (mostCommonSubStr != candidateSubstr) {
                    toRemove.add(candidate)
                }

            }
            oxygenCandidates.removeAll(toRemove)
            subStringSize++

        }

        val co2Candidates: MutableList<List<Int>> = ArrayList(input)
        var leastCommonSubStr = ""
        subStringSize = 1;

        while (co2Candidates.size > 1) {
            val toRemove = ArrayList<List<Int>>()

            var sum = 0.0
            for (row in co2Candidates) {
                sum += row[subStringSize - 1]
            }

            val leastCommonBit = if ((sum / co2Candidates.size.toDouble()).roundToInt() == 1) 0 else 1
            leastCommonSubStr += leastCommonBit

            for (candidate in co2Candidates) {
                val candidateSubstr = candidate.joinToString("").substring(0, subStringSize)
                if (sum == 0.5 && !candidateSubstr.endsWith("0")) {
                    toRemove.add(candidate)
                } else if (leastCommonSubStr != candidateSubstr) {
                    toRemove.add(candidate)
                }
            }
            co2Candidates.removeAll(toRemove)
            subStringSize++

        }

        val oxygen = oxygenCandidates[0].joinToString("").toInt(2)
        val co2 = co2Candidates[0].joinToString("").toInt(2)
        return oxygen * co2
    }

}