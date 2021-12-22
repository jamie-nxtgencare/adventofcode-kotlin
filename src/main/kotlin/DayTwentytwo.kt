class DayTwentytwo(file: String) : Project {
    val ins = mapFileLines(file) {
        val chunks = it.split(",")

        RebootInstruction(
            it.substring(0,2) == "on",
            chunks[0].split("=").last(),
            chunks[1].split("=").last(),
            chunks[2].split("=").last()
        )
    }

    lateinit var grid: HashMap<Triple<Int, Int, Int>, Boolean>

    override fun part1(): Any {
        grid = HashMap()
        ins.forEach {
            for(x in it.zIntRange) {
                if (inRange(x)) {
                    for (y in it.yIntRange) {
                        if (inRange(y)) {
                            for (z in it.xIntRange) {
                                if (inRange(z)) {
                                    if (it.on) {
                                        grid[Triple(x, y, z)] = true
                                    } else {
                                        grid.remove(Triple(x, y, z))
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return grid.size
    }

    override fun part2(): Any {
        grid = HashMap()
        return -1
    }

    private fun inRange(i: Int): Boolean {
        return i <= 50 && i >= -50
    }

}

class RebootInstruction(val on: Boolean, xRangeS: String, yRangeS: String, zRangeS: String) {
    val xIntRange = toIntRange(xRangeS)
    val yIntRange = toIntRange(yRangeS)
    val zIntRange = toIntRange(zRangeS)

    private fun toIntRange(s: String): IntRange {
        val split = s.split("..")
        return IntRange(split.first().toInt(), split.last().toInt())
    }
}