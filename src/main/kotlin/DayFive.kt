import BoardingPass.Constants.Direction.Companion.byLetter

class DayFive(file: String) : Project {
    private val passes : List<BoardingPass> = getPasses(file)
    private val seats = passes.map { it.traverse() }

    private fun getPasses(file: String) : List<BoardingPass> {
        return getLines(file).map { BoardingPass(it.split("").filter { it2 -> it2 != "" }) }
    }

    override fun part1(): Int {
        return seats.maxOrNull() ?: -1
    }

    override fun part2(): Int {
        var lastPresent = false

        for (i in 0..1023) {
            if (lastPresent && !seats.contains(i) && seats.contains(i+1)) {
                return i
            }
            lastPresent = seats.contains(i)
        }

        return -1
    }
}

class BoardingPass(directionStrings: List<String>) {
    private val directions: Map<Boolean, List<Direction>> = directionStrings.map { byLetter(it) }.groupBy { it == Direction.B || it == Direction.F }

    companion object Constants {
        enum class Direction {
            B,
            F,
            L,
            R;

            companion object { fun byLetter(letter: String) : Direction { return values().findLast { it.toString() == letter } ?: B } }
        }
    }

    fun traverse(): Int {
        var range =  0..127

        directions[true]?.forEach {
            range = getSeatRange(it, range)
        }

        val row = range.first

        range =  0..7

        directions[false]?.forEach {
            range = getSeatRange(it, range)
        }

        val column = range.first

        return row * 8 + column
    }

    fun getSeatRange(direction: Direction, range : IntRange): IntRange {
        val mid = (range.first+range.last) / 2
        return if (direction == Direction.F || direction == Direction.L) range.first..mid else mid+1..range.last
    }
}
