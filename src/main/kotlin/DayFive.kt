import java.lang.Integer.min

class DayFive(file: String) : Project {
    private val lines = mapFileLines(file) { Line(it) }

    override fun part1(): Any {
        val maxX = lines.maxOf { it.maxX() }
        val maxY = lines.maxOf { it.maxY() }

        val grid = Array(maxX+1) { Array(maxY+1) { 0 }}

        lines.forEach { it.drawOn(1, grid) }

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

    override fun part2(): Any {
        return -1
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

        for (x in min(x, x2)..x.coerceAtLeast(x2)) {
            for (y in min(y, y2)..y.coerceAtLeast(y2)) {
                grid[x][y] = grid[x][y] + 1
            }
        }
    }
}
