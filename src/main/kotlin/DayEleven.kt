class DayEleven(file: String) : Project {
    private val debug = false
    private var originalGrid = mapLettersPerLines(file) { it.map { it2 -> Seat.fromLetter(it2) }.toMutableList() }.toMutableList()
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

        val changed = grid != grid2
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

    private fun clone(toCopy: MutableList<MutableList<Seat>>): MutableList<MutableList<Seat>> {
        val copy: MutableList<MutableList<Seat>> = ArrayList()

        toCopy.forEach {
            val list = ArrayList<Seat>()
            it.forEach { it2 ->
                list.add(it2)
            }
            copy.add(list)
        }

        return copy
    }

    fun printGrid() {
        grid.forEach {
            it.forEach { it2 ->
                print(it2.symbol)
            }
            println()
        }
    }

    enum class Seat(symbol: Char) {
        OCCUPIED('#'),
        EMPTY('L'),
        FLOOR('.');

        val symbol: Char = symbol

        companion object {
            fun fromLetter(letter: Char) : Seat {
                return if (letter == '.') FLOOR else if (letter == 'L') EMPTY else OCCUPIED
            }
        }
    }

}