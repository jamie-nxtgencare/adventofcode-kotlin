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

    override fun part1(): Any {
        var count = 0
        val range = -50..50
        for (x in range) {
            for (y in range) {
                for (z in range) {
                    var on = false
                    for (i in ins) {
                        if (i.xIntRange.contains(x) && i.yIntRange.contains(y) && i.zIntRange.contains(z)) {
                            on = i.on
                        }
                    }
                    if (on) {
                        count++
                    }
                }
            }
        }
        return count
    }

    override fun part2(): Any {
        return -1
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