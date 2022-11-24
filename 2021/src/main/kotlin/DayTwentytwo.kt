import kotlin.math.min

class DayTwentytwo(file: String) : Project {
    var i = 0
    val ins = mapFileLines(file) {
        val chunks = it.split(",")

        RebootInstruction(
            it.substring(0,2) == "on",
            chunks[0].split("=").last(),
            chunks[1].split("=").last(),
            chunks[2].split("=").last(),
            i++
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
        val prisms = ArrayList<RectangularPrism>()
        var count = 1
        var prev = 0L
        for (i in ins) {
            val area = i.prism.area()
            println("Turning ${if (i.on) "on" else "off"} up to $area cubes")
            val overlappingPrisms = prisms.filter { it.overlaps(i.prism) }
            prisms.removeAll(overlappingPrisms.toSet())

            if (i.on) {
                if (overlappingPrisms.isEmpty()) {
                    prisms.add(i.prism)
                } else {
                    prisms.addAll(overlappingPrisms.map { it.add(i.prism) }.flatten().toSet())
                }
            } else {
                prisms.addAll(overlappingPrisms.map { it.subtract(i.prism) }.flatten())
            }
            val cubes = prisms.sumOf { it.area() }
            println("${count++} and ${prisms.size} prisms (count: $cubes, prev $prev, diff ${cubes - prev})")

            if (area < cubes - prev) {
                println("Houston we have a problem")
            }

            prev = cubes
        }

        return prisms.sumOf { it.area() }
    }
}

class RebootInstruction(val on: Boolean, xRangeS: String, yRangeS: String, zRangeS: String, val order: Int) {
    val xIntRange = toLongRange(xRangeS)
    val yIntRange = toLongRange(yRangeS)
    val zIntRange = toLongRange(zRangeS)
    val prism = RectangularPrism(
        xIntRange.first,
        yIntRange.first,
        zIntRange.first,
        xIntRange.last,
        yIntRange.last,
        zIntRange.last
    )

    private fun toLongRange(s: String): LongRange {
        val split = s.split("..")
        return LongRange(split.first().toLong(), split.last().toLong())
    }
}

class RectangularPrism(var x: Long, var y: Long, var z: Long, var x2: Long, var y2: Long, var z2: Long, val label: String = "") {
    fun area() = length() * width() * height()
    private fun width() =  x2 - x + 1
    private fun length() = y2 - y + 1
    private fun height() = z2 - z + 1

    fun overlaps(prism: RectangularPrism): Boolean {
        return (x <= prism.x2) && (prism.x <= x2) &&
                (y <= prism.y2) && (prism.y <= y2) &&
                (z <= prism.z2) && (prism.z <= z2)
    }

    fun intersection(prism: RectangularPrism): RectangularPrism? {
        return if (overlaps(prism)) RectangularPrism(
            x.coerceAtLeast(prism.x),
            y.coerceAtLeast(prism.y),
            z.coerceAtLeast(prism.z),
            min(x2, prism.x2),
            min(y2, prism.y2),
            min(z2, prism.z2),
        ) else null
    }

    fun add(prism: RectangularPrism): List<RectangularPrism> {
        val out = ArrayList<RectangularPrism>()

        // Find intersection
        val intersection = intersection(prism)

        if (intersection != null) {
            // Remove the interesection from one side
            out.addAll(subtract(intersection))
            // Add the other side back in
            out.add(prism)
        } else {
            out.add(this)
            out.add(prism)
        }

        return out
    }

    fun subtract(prism: RectangularPrism): List<RectangularPrism> {
        val out = ArrayList<RectangularPrism>()
        // Find intersection
        val intersection = intersection(prism)

        if (intersection != null) {
            var topBottom = z2
            // Does this prism extend above the intersection?
            if (z2 > intersection.z2) {
                topBottom = intersection.z2 + 1
                out.add(
                    RectangularPrism(
                        x,
                        y,
                        topBottom,
                        x2,
                        y2,
                        z2,
                        "top"
                    )
                )
                topBottom--
            }

            // Does this prism extend below the intersection?
            var bottomTop = z
            if (z < intersection.z) {
                bottomTop = intersection.z - 1
                out.add(
                    RectangularPrism(
                        x,
                        y,
                        z,
                        x2,
                        y2,
                        bottomTop,
                        "bottom"
                    )
                )
                bottomTop++
            }

            var rightLeft = x2
            // Does this prism extend to the left of the intersection?
            if (x2 > intersection.x2) {
                rightLeft = intersection.x2 + 1
                out.add(
                    RectangularPrism(
                        rightLeft,
                        y,
                        bottomTop,
                        x2,
                        y2,
                        topBottom,
                        "right"
                    )
                )
                rightLeft--
            }

            var leftRight = x
            // Does this prism extend to the right of the intersection?
            if (x < intersection.x) {
                leftRight = intersection.x - 1
                out.add(
                    RectangularPrism(
                        x,
                        y,
                        bottomTop,
                        leftRight,
                        y2,
                        topBottom,
                        "left"
                    )
                )
                leftRight++
            }

            // Does this prism extend to the back of the intersection?
            if (y2 > intersection.y2) {
                val frontBack = intersection.y2 + 1
                out.add(
                    RectangularPrism(
                        leftRight,
                        frontBack,
                        bottomTop,
                        rightLeft,
                        y2,
                        topBottom,
                        "back"
                    )
                )
            }

            // Does this prism extend to the front of the intersection?
            if (y < intersection.y) {
                val backFront = intersection.y - 1
                out.add(
                    RectangularPrism(
                        leftRight,
                        y,
                        bottomTop,
                        rightLeft,
                        backFront,
                        topBottom,
                        "front"
                    )
                )
            }

        } else {
            out.add(this)
        }

        return out
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is RectangularPrism) return false

        if (x != other.x) return false
        if (y != other.y) return false
        if (z != other.z) return false
        if (x2 != other.x2) return false
        if (y2 != other.y2) return false
        if (z2 != other.z2) return false

        return true
    }

    override fun hashCode(): Int {
        var result = x
        result = 31 * result + y
        result = 31 * result + z
        result = 31 * result + x2
        result = 31 * result + y2
        result = 31 * result + z2
        return result.toInt()
    }

    override fun toString(): String {
        return "RectangularPrism(x=$x..$x2, y=$y..$y2, z=$z..$z2)${if (label.isBlank()) "" else "($label)"}"
    }


}