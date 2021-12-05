import java.lang.Integer.min

class DayFive(file: String) : Project {
    private val lines = mapFileLines(file) { Line(it) }

    override fun part1(): Any {
        return getAnswer(1)
    }

    override fun part2(): Any {
        return getAnswer(2)
    }

    private fun getAnswer(part: Int): Int {
        val maxX = lines.maxOf { it.maxX() }
        val maxY = lines.maxOf { it.maxY() }

        val grid = Array(maxX + 1) { Array(maxY + 1) { 0 } }

        lines.forEach { it.drawOn(part, grid) }

        var result = 0
        for (x in grid.indices) {
            for (y in grid[x].indices) {
                if (grid[x][y] > 1) {
                    result++
                }
            }
        }

        return result
    }
}

class Line(it: String) {
    private val x: Int
    private val y: Int
    private val x2: Int
    private val y2: Int

    init {
        val startAndFinish = it.split(" -> ")
        val start = startAndFinish[0].split(",")
        x = start[0].toInt()
        y = start[1].toInt()
        val end = startAndFinish[1].split(",")
        x2 = end[0].toInt()
        y2 = end[1].toInt()
    }

    fun maxX(): Int {
        return if (x > x2) x else x2
    }

    fun maxY(): Int {
        return if (y > y2) y else y2
    }

    private fun isSloped(): Boolean {
        return x != x2 && y != y2
    }

    fun drawOn(part: Int, grid: Array<Array<Int>>) {
        if (part == 1 && isSloped()) {
            return
        }

        var xPrime = x;
        var yPrime = y;
        grid[xPrime][yPrime] = grid[xPrime][yPrime] + 1

        while (xPrime != x2 || yPrime != y2) {
            xPrime = if (x < x2) xPrime + 1 else if (x == x2) xPrime else xPrime - 1
            yPrime = if (y < y2) yPrime + 1 else if (y == y2) yPrime else yPrime - 1
            grid[xPrime][yPrime] = grid[xPrime][yPrime] + 1
        }
    }
}
