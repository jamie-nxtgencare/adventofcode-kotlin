@file:Suppress("PackageName")

package `2020`

import Project

class DayEleven(file: String) : Project {
    private val debug = false
    private var originalGrid = mapLettersPerLines(file) { it.map { it2 -> Seat.fromLetter(it2) }.toTypedArray() }.toTypedArray()
    private var grid = clone(originalGrid)

    override fun part1(): Any {
        if (debug) {
            printGrid()
        }

        go(1)

        return countOccupied()
    }
    override fun part2(): Any {
        if (debug) {
            printGrid()
        }

        go(2)

        return countOccupied()
    }

    private fun go(i: Int) {
        grid = clone(originalGrid)
        while (update(i)) {
            if (debug) {
                println()
                printGrid()
            }
        }
    }

    private fun update(i: Int): Boolean {
        val grid2 = clone(grid)

        grid.forEachIndexed { y, yit ->
            yit.forEachIndexed { x, _ ->
                if (i == 1) {
                    grid2[y][x] = next(y, x)
                } else {
                    grid2[y][x] = next2(y, x)
                }
            }
        }

        val changed = !grid.contentDeepEquals(grid2)
        grid = grid2

        return changed
    }

    private fun countOccupied(): Int {
        var count = 0
        grid.forEach {
            it.forEach { it2 ->
                if (it2 == Seat.OCCUPIED) {
                    count++
                }
            }
        }

        return count
    }

    private fun next(y: Int, x: Int): Seat {
        val seat = get(y, x)
        val surroundings: List<Seat?> = listOf(
            get(y-1,x-1), get(y-1,x), get(y-1,x+1),
            get(y,x-1), /*get(y,x),*/ get(y,x+1),
            get(y+1,x-1), get(y+1,x), get(y+1,x+1)
        )

        val countOccupied = surroundings.filter { it?.equals(Seat.OCCUPIED) ?: false }.size

        if (seat?.equals(Seat.EMPTY) == true && countOccupied == 0) {
            return Seat.OCCUPIED
        } else if (seat?.equals(Seat.OCCUPIED) == true && countOccupied >= 4) {
            return Seat.EMPTY
        }

        return seat ?: Seat.EMPTY
    }

    private fun get(y: Int, x: Int): Seat? {
        return grid.getOrNull(y)?.getOrNull(x)
    }

    private fun next2(y: Int, x: Int): Seat {
        val seat = get(y, x)
        val surroundings: List<Seat?> = listOf(
            walk(y, x, -1, -1), walk(y, x, -1, 0), walk(y, x, -1, 1),
            walk(y, x, 0, -1), /*walk(y, x, 0, 0),*/ walk(y, x, 0, 1),
            walk(y, x, 1, -1), walk(y, x, 1, 0), walk(y, x, 1, 1),
        )

        val countOccupied = surroundings.filter { it?.equals(Seat.OCCUPIED) ?: false }.size

        if (seat?.equals(Seat.EMPTY) == true && countOccupied == 0) {
            return Seat.OCCUPIED
        } else if (seat?.equals(Seat.OCCUPIED) == true && countOccupied >= 5 /* dirty gotcha */) {
            return Seat.EMPTY
        }

        return seat ?: Seat.EMPTY
    }

    private fun walk(y: Int, x: Int, yStep: Int, xStep: Int): Seat? {
        val newY = y+yStep
        val newX = x+xStep

        val space = grid.getOrNull(newY)?.getOrNull(newX)

        if (space == Seat.FLOOR) {
            return walk(newY, newX, yStep, xStep)
        }

        return space
    }

    private fun clone(toCopy: Array<Array<Seat>>): Array<Array<Seat>> {
       return toCopy.copyOf().map { it.copyOf() }.toTypedArray()
    }

    fun printGrid() {
        grid.forEach {
            it.forEach { it2 ->
                print(it2.symbol)
            }
            println()
        }
    }

    enum class Seat(val symbol: Char) {
        OCCUPIED('#'),
        EMPTY('L'),
        FLOOR('.');

        companion object {
            fun fromLetter(letter: Char) : Seat {
                return if (letter == '.') FLOOR else if (letter == 'L') EMPTY else OCCUPIED
            }
        }
    }

}