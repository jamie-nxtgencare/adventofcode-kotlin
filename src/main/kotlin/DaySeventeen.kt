class DaySeventeen(file: String) : Project {
    private val debug = false
    private var size = 100
    private var grid: Array<Array<Array<Cube?>>> = getEmptyGrid()
    private var originalGrid3D: Array<Array<Array<Cube?>>> = getEmptyGrid()
    private var originalGrid2D = mapLettersPerLines(file) { it.map { it2 -> Cube.fromLetter(it2) }.toTypedArray() }.toTypedArray()

    private fun getEmptyGrid(): Array<Array<Array<Cube?>>> {
        val z:MutableList<Array<Array<Cube?>>> = ArrayList()

        for (i in 0 until size) {
            val y:MutableList<Array<Cube?>> = ArrayList()
            for (i in 0 until size) {
                y.add(arrayOfNulls<Cube?>(size))
            }

            z.add(y.toTypedArray())
        }

        return z.toTypedArray()
    }

    init {
        originalGrid2D.forEachIndexed { y, yit ->
            yit.forEachIndexed { x, xit ->
                originalGrid3D[size/2][y+size/2][x+size/2] = xit
            }
        }
    }

    override fun part1(): Any {
        go(6)
        return countActive()
    }
    override fun part2(): Any {
        return -1
    }

    private fun go(count: Int) {
        grid = clone(originalGrid3D)
        for (i in 0 until count) {
            /*println("Cycle $i")
            countActive()*/
            update()
        }
    }

    private fun update() {
        val grid2 = clone(grid)

        grid.forEachIndexed { z, zit ->
            zit.forEachIndexed { y, yit ->
                yit.forEachIndexed { x, _ ->
                    grid2[z][y][x] = next(z, y, x)
                }
            }
        }

        grid = grid2
    }

    private fun countActive(): Int {
        var count = 0
        grid.forEachIndexed { i, it ->
            var zi = 0
            it.forEach { it2 ->
                it2.forEach { it3 ->
                    if (it3 == Cube.ACTIVE) {
                        count++
                        zi++
                    }
                }
            }
/*            if (zi != 0) {
                println("z$i: $zi")
                printGrid(i)
            }*/
        }

        return count
    }

    private fun next(z: Int, y: Int, x: Int): Cube {
        val seat = get(z, y, x)
        val surroundings: List<Cube?> = listOf(
            get(z-1,y-1,x-1), get(z-1,y-1,x), get(z-1,y-1,x+1),
            get(z-1,y,x-1), get(z-1,y,x), get(z-1,y,x+1),
            get(z-1,y+1,x-1), get(z-1,y+1,x), get(z-1,y+1,x+1),

            get(z,y-1,x-1), get(z,y-1,x), get(z,y-1,x+1),
            get(z,y,x-1), /*get(z,y,x),*/ get(z,y,x+1),
            get(z,y+1,x-1), get(z,y+1,x), get(z,y+1,x+1),

            get(z+1,y-1,x-1), get(z+1,y-1,x), get(z+1,y-1,x+1),
            get(z+1,y,x-1), get(z+1,y,x), get(z+1,y,x+1),
            get(z+1,y+1,x-1), get(z+1,y+1,x), get(z+1,y+1,x+1)
        )

        val countActive = surroundings.filter { it?.equals(Cube.ACTIVE) ?: false }.size

        val active = seat?.equals(Cube.ACTIVE) ?: false

        if (z == 0 && y == 11 && x == 12) {
            //println("okay")
        }

        if (active) {
            return if (countActive == 2 || countActive == 3) Cube.ACTIVE else Cube.INACTIVE
        }

        return if (countActive == 3) Cube.ACTIVE else Cube.INACTIVE
    }

    private fun get(z: Int, y: Int, x: Int): Cube? {


        return grid.getOrNull(z)?.getOrNull(y)?.getOrNull(x)
    }

    private fun clone(toCopy: Array<Array<Array<Cube?>>>): Array<Array<Array<Cube?>>> {
        return toCopy.copyOf().map { it.copyOf().map { it2 -> it2.copyOf() }.toTypedArray()}.toTypedArray()
    }

    fun printGrid(i: Int) {
        grid[i].forEachIndexed { y, it ->
            print((y%10).toString() + " :")
            it.forEach {it2 ->
                print((it2?.symbol ?: '.'))
            }
            println()
        }
        println("----========-----------")
        println("----========-----------")
        println("----========-----------")

    }

    enum class Cube(val symbol: Char) {
        ACTIVE('#'),
        INACTIVE('.');

        companion object {
            fun fromLetter(letter: Char) : Cube {
                return if (letter == '#') ACTIVE else INACTIVE
            }
        }
    }

}