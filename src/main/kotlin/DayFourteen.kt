import kotlin.math.pow

class DayFourteen (file: String) : Project {
    private val instructions = mapFileLines(file) { getInstructions(it) }
    private var mask = 0L
    private var overwrite = 0L
    private var addressMask = 0L
    private var addressOverwrites = ArrayList<Long>()

    private val mem = HashMap<Long, Long>()

    private fun getInstructions(instructionStr: String): (part: Int) -> Unit {
        if (instructionStr.startsWith("mask = ")) {
            return { setMask(instructionStr.split("= ")[1]) }
        }

        return { part ->
            val address = instructionStr.split("[")[1].split("]")[0].toLong()
            val value = instructionStr.split("= ")[1].toLong()

            if (part == 1) {
                assignValue(address, value)
            } else {
                assignValue2(address, value)
            }
        }
    }

    private fun assignValue(address: Long, value: Long) {
        val out = mask and value or overwrite
        mem[address] = out
    }

    private fun assignValue2(address: Long, value: Long) {
        addressOverwrites.forEach {
            val newAddress = addressMask and address or it
            mem[newAddress] = value
        }
    }

    private fun setMask(maskStr: String) {
        mask = maskStr.replace("\\d".toRegex(), "0").replace("X", "1").toLong(2)
        overwrite = maskStr.replace("X".toRegex(), "0").toLong(2)

        addressMask = maskStr.replace("X", "T").replace("1", "T").replace("0", "1").replace("T", "0").toLong(2)
        addressOverwrites.clear()

        val maskedBits = maskStr.count { it == 'X' }
        val count = 2.toDouble().pow(maskedBits.toDouble()).toInt()

        // 1 masked bit = X 0 or X 1 (0 to 2^1)
        // 2 masked bits = 00 01 10 11 (0 to 2^2)
        for (i in 0..count) {
            val binStr = Integer.toBinaryString(i).padStart(maskedBits, '0')
            var newMaskStr = maskStr
            binStr.forEach {
                newMaskStr = newMaskStr.replaceFirst('X', it)
            }
            addressOverwrites.add(newMaskStr.toLong(2))
        }
    }

    override fun part1(): Any {
        instructions.forEach { it(1) }
        return mem.values.sum()
    }

    override fun part2(): Any {
        mem.clear()
        instructions.forEach { it(2) }
        return mem.values.sum()
    }
}