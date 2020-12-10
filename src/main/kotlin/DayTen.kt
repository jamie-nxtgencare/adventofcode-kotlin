import java.math.BigInteger

class DayTen(file: String) : Project {
    private val input = mapFileLines(file) { it.toInt() }.sorted()
    private val differences = input.mapIndexed { i, it -> it - (if (i == 0) 0 else input[i - 1]) }.toMutableList()

    override fun part1(): Any {
        val m = HashMap<Int, Int>()
        differences.forEach { m[it] = m.computeIfAbsent(it) { 0 } + 1 }
        return m[1]?.times((m[3] ?: -1) + 1) ?: -1
    }
    override fun part2(): Any {
        differences.add(3)

        var count = 0
        var product = BigInteger.valueOf(1)
        differences.forEach{
            if (it == 3) {
                product = product.times(BigInteger.valueOf(if (count == 4) 7 else if (count == 3) 4 else if (count == 2) 2 else 1))
                count = 0
            } else {
                count++
            }
        }
        return product
    }
}