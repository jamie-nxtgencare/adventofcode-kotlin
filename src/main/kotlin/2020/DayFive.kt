@file:Suppress("PackageName")

package `2020`

import Project

class DayFive(file: String, isTest: Boolean = false) : Project(file, isTest) {
    private val passes : List<BoardingPass> = mapLettersPerLines(file) { BoardingPass( it ) }
    private val seats = passes.map { it.traverse() }

    override suspend fun part1(): Any {
        return seats.maxOrNull() ?: -1
    }

    override suspend fun part2(): Any {
        var lastPresent = false

        for (i in 0..1023) {
            val present = seats.contains(i)
            if (!present && lastPresent && seats.contains(i + 1)) {
                return i
            }
            lastPresent = present
        }

        return -1
    }
}

class BoardingPass(directionStrings: List<Char>) {
    private val directions: Map<Boolean, List<Direction>> = directionStrings.map { Direction.byLetter(it) }.groupBy { it == Direction.B || it == Direction.F }

    companion object Constants {
        enum class Direction {
            B,
            F,
            L,
            R;

            companion object { fun byLetter(letter: Char) : Direction { return values().findLast { it.toString().toCharArray()[0] == letter } ?: B } }
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
