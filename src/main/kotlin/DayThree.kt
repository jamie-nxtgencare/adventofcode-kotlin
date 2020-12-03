class DayThree(file: String) : Project {
    private val grid = getGrid(file)

    private fun getGrid(file: String): Array<Array<Boolean>> {
        return getLines(file).map { it.split("").filter { it2 -> it2 != "" }.map { it3 -> it3 == "#" } .toTypedArray() }.toTypedArray()
    }

    override fun part1(): Int {
        return traverse(3)
    }

    private fun traverse(xSlope: Int) = traverse(xSlope, 1)

    private fun traverse(xSlope: Int, ySlope: Int): Int {
        var out = 0
        for (i in 0 until grid.size / ySlope) {
            val row = grid[i * ySlope]
            if (row[i * xSlope % row.size]) {
                out++
            }
        }
        return out
    }

    override fun part2(): Int {
        return traverse(1) * traverse(3) * traverse(5) * traverse(7) * traverse(1, 2)
    }

}